package sollecitom.libs.pillar.web.api.utils.filters.correlation

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.http4k.core.*
import org.http4k.lens.Header
import org.http4k.lens.bearerToken
import org.http4k.lens.boolean
import org.http4k.lens.locale
import sollecitom.libs.pillar.acme.business.domain.Example
import sollecitom.libs.pillar.jwt.domain.AcmeJwtScheme
import sollecitom.libs.pillar.web.api.utils.api.withInvocationContext
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.identity.fromString
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.UserAccount
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.http4k.utils.ipAddress
import sollecitom.libs.swissknife.http4k.utils.lens.USER_AGENT
import sollecitom.libs.swissknife.jwt.domain.JWT
import sollecitom.libs.swissknife.jwt.domain.JwtParty
import sollecitom.libs.swissknife.jwt.domain.JwtProcessor
import sollecitom.libs.swissknife.jwt.domain.isValidAtTime
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessor
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessorConfiguration
import sollecitom.libs.swissknife.lens.core.extensions.identity.id
import sollecitom.libs.swissknife.lens.correlation.extensions.toggles.toggleValue
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import sollecitom.libs.swissknife.web.client.info.analyzer.ClientInfoAnalyzer
import sollecitom.libs.swissknife.web.client.info.analyzer.instance
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

context(api: HttpApiDefinition, random: RandomGenerator, time: TimeGenerator, ids: UniqueIdGenerator)
fun InvocationContextFilter.parseInvocationContextFromRequest(jwtProcessorConfiguration: JwtProcessor.Configuration = newJwtProcessorConfiguration(), cacheExpiry: Duration = GatewayInvocationContextFilter.defaultCacheExpiry, cacheMaximumSize: Long = GatewayInvocationContextFilter.defaultCacheMaximumSize, issuerForDomain: (String) -> JwtParty): Filter = parseInvocationContextFromRequest(headerNames = api.headerNames, cacheExpiry = cacheExpiry, cacheMaximumSize = cacheMaximumSize, issuerForDomain = issuerForDomain, jwtProcessorConfiguration = jwtProcessorConfiguration, randomGenerator = random, timeGenerator = time, uniqueIdGenerator = ids)

fun InvocationContextFilter.parseInvocationContextFromRequest(headerNames: HttpHeaderNames, cacheExpiry: Duration = GatewayInvocationContextFilter.defaultCacheExpiry, cacheMaximumSize: Long = GatewayInvocationContextFilter.defaultCacheMaximumSize, issuerForDomain: (String) -> JwtParty, jwtProcessorConfiguration: JwtProcessor.Configuration, randomGenerator: RandomGenerator, timeGenerator: TimeGenerator, uniqueIdGenerator: UniqueIdGenerator): Filter = GatewayInvocationContextFilter(headerNames, cacheExpiry, cacheMaximumSize, jwtProcessorConfiguration, issuerForDomain, randomGenerator, timeGenerator, uniqueIdGenerator)

internal class GatewayInvocationContextFilter(override val headerNames: HttpHeaderNames, private val cacheExpiry: Duration, private val cacheMaximumSize: Long, private val jwtProcessorConfiguration: JwtProcessor.Configuration, private val issuerForDomain: (String) -> JwtParty, randomGenerator: RandomGenerator, timeGenerator: TimeGenerator, uniqueIdGenerator: UniqueIdGenerator) : Filter, HttpApiDefinition, RandomGenerator by randomGenerator, TimeGenerator by timeGenerator, UniqueIdGenerator by uniqueIdGenerator {

    private val clientInfoAnalyzer = ClientInfoAnalyzer.instance
    private val properties = Properties(headerNames)
    private val verifiedJwts = CacheBuilder.newBuilder().maximumSize(cacheMaximumSize).expireAfterWrite(cacheExpiry.toJavaDuration()).build<String, JWT>()

    override fun invoke(next: HttpHandler) = { request: Request ->

        runCatching { invocationContext(request) }.map { invocationContext -> next(request.withoutAuthorization().withInvocationContext(invocationContext)) }.getOrElse { it.asResponse() }
    }

    private fun Request.withoutAuthorization(): Request = removeHeader("Authorization")

    private fun invocationContext(request: Request): InvocationContext<Access> {

        val authority = request.uri.authority
        val bearerToken = request.bearerToken()
        val specifiedLocale = parseSpecifiedLocale(request)
        val origin = parseOrigin(request)
        val specifiedTargetCustomer = parseSpecifiedTargetCustomer(request)
        val toggles = parseToggles(request)
        val trace = parseTrace(request)
        val specifiedTargetTenant = parseSpecifiedTargetTenant(request)
        if (bearerToken != null) {
            val jwt = verifiedJwts.forTokenAndDomain(bearerToken, authority)
            val jwtParams = AcmeJwtScheme.parseParametersFromClaims(jwt.claimsAsJson)
            return jwtParams.invocationContext(origin, trace, toggles, specifiedLocale, specifiedTargetCustomer, specifiedTargetTenant)
        }
        return request.unauthenticatedInvocationContext(origin, specifiedTargetCustomer, trace, toggles, specifiedLocale, specifiedTargetTenant)
    }

    private fun Cache<String, JWT>.forTokenAndDomain(token: String, domain: String): JWT = getIfNotExpired(token) ?: createAndCache(token) { parseAndVerifyJwt(token, domain) }

    private fun Cache<String, JWT>.createAndCache(token: String, create: () -> JWT): JWT {

        val jwt = create()
        put(token, jwt)
        return jwt
    }

    private fun Cache<String, JWT>.getIfNotExpired(token: String) = getIfPresent(token)?.takeIf { it.isNotExpired() }

    private fun JWT.isNotExpired(): Boolean = isValidAtTime(time = clock.now())

    private fun Request.unauthenticatedInvocationContext(origin: Origin, specifiedTargetCustomer: Customer?, trace: Trace, toggles: Toggles, specifiedLocale: Locale?, specifiedTargetTenant: Tenant?): InvocationContext<Access.Unauthenticated> {

        val authorization = AuthorizationPrincipal(roles = unauthenticatedRoles(this))
        val scope = unauthenticatedAccessScope(this)
        val access = Access.Unauthenticated(origin, authorization, scope, parseIsTest(this, specifiedTargetCustomer))
        return InvocationContext(access, trace, toggles, specifiedLocale, specifiedTargetCustomer, specifiedTargetTenant)
    }

    private fun AcmeJwtScheme.Parameters.invocationContext(origin: Origin, trace: Trace, toggles: Toggles, specifiedLocale: Locale?, specifiedTargetCustomer: Customer?, specifiedTargetTenant: Tenant?): InvocationContext<Access.Authenticated> {

        val actor = actor(specifiedLocale)
        val authorization = AuthorizationPrincipal(roles = roles())
        val scope = accessScope()
        val access = actor.let { Access.Authenticated(it, origin, authorization, scope) }
        return InvocationContext(access, trace, toggles, specifiedLocale, specifiedTargetCustomer, specifiedTargetTenant)
    }

    private fun AcmeJwtScheme.Parameters.actor(specifiedLocale: Locale?): Actor { // TODO add support for service accounts, user locale, other actor types, other authentication mechanisms, etc. after we'll support these with JWT custom attributes

        val tokenId = token.id.let(Id.Companion::fromString)
        val sessionId = session.id.let(Id.Companion::fromString)
        val customerId = user.organization.id.let(Id.Companion::fromString)
        val userId = user.id.let(Id.Companion::fromString)
        val customer = Customer(id = customerId, isTest = false) // TODO look this up from an injected function
        val userAccount = UserAccount(id = userId, locale = specifiedLocale, customer = customer, tenant = Example.tenant)
        val authentication = CredentialsBasedAuthentication(token = Authentication.Token(id = tokenId, validFrom = token.issuedAt, validTo = token.expiresAt), session = SimpleSession(id = sessionId))
        return DirectActor(account = userAccount, authentication = authentication)
    }

    private fun AcmeJwtScheme.Parameters.roles(): Roles = access.applicationRoles.map(::Name).map(::Role).toSet().let(::Roles)

    private fun AcmeJwtScheme.Parameters.accessScope(): AccessScope = AccessScope(containerStack = emptyList()) // TODO add support for access scopes after we'll support this in JWT via custom attributes

    private fun parseToggles(request: Request): Toggles = properties.toggleValues(request).let(List<ToggleValue<*>>::toSet).let(::Toggles)

    private fun parseOrigin(request: Request): Origin = Origin(ipAddress = request.ipAddress, clientInfo = request.clientInfo())

    private fun Request.clientInfo(): ClientInfo = Header.USER_AGENT(this)?.let { clientInfoAnalyzer.invoke(it) } ?: ClientInfo.unknown

    private fun customerWithId(id: Id): Customer = Customer(id = id, isTest = false) // TODO look this up from an injected function

    private fun unauthenticatedRoles(request: Request): Roles = Roles(values = emptySet())

    private fun parseSpecifiedLocale(request: Request): Locale? = properties.specifiedLocale(request)

    private fun parseSpecifiedTargetTenant(request: Request): Tenant? = properties.specifiedTargetTenant(request)

    private fun parseSpecifiedTargetCustomer(request: Request): Customer? {

        val specifiedTargetCustomerId = properties.specifiedTargetCustomerId(request)
        return specifiedTargetCustomerId?.let { customerWithId(it) }
    }

    private fun parseTrace(request: Request): Trace {

        val externalInvocationId = properties.externalInvocationId(request)
        val externalActionId = properties.externalActionId(request)
        val externalTrace = ExternalInvocationTrace(invocationId = externalInvocationId, actionId = externalActionId)
        val trace = InvocationTrace(id = newId(), createdAt = clock.now())
        return Trace(invocation = trace, external = externalTrace)
    }

    private fun unauthenticatedAccessScope(request: Request): AccessScope = AccessScope(containerStack = emptyList())

    private fun parseIsTest(request: Request, specifiedTargetCustomer: Customer?): Boolean {

        return specifiedTargetCustomer?.isTest ?: properties.isTest(request) ?: false
    }

    private fun parseAndVerifyJwt(token: String, domain: String): JWT {

        val issuer = issuerForDomain(domain)
        val processor = newJwtProcessor(issuer, jwtProcessorConfiguration)
        return processor.readAndVerify(token)
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

    private class Properties(headerNames: HttpHeaderNames) {

        val externalInvocationId = Header.id().required(name = headerNames.gateway.externalInvocationId)
        val externalActionId = Header.id().required(name = headerNames.gateway.externalActionId)
        val specifiedLocale = Header.locale().optional(name = headerNames.gateway.specifiedLocale)
        val specifiedTargetTenant = Header.id().map(::Tenant).optional(name = headerNames.gateway.specifiedTargetTenant)
        val specifiedTargetCustomerId = Header.id().optional(name = headerNames.gateway.specifiedTargetCustomerId)
        val isTest = Header.boolean().optional(name = headerNames.gateway.isTest)
        val toggleValues = Header.toggleValue().multi.defaulted(name = headerNames.gateway.toggles, default = emptyList())
    }

    companion object {
        const val defaultCacheMaximumSize = 500L
        val defaultCacheExpiry = 30.minutes
    }
}