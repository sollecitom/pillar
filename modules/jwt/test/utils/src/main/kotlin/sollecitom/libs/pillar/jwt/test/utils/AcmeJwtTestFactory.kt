package sollecitom.libs.pillar.jwt.test.utils

import sollecitom.libs.pillar.jwt.domain.*
import sollecitom.libs.pillar.acme.business.domain.AcmeRoles
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.kotlin.extensions.time.truncatedToSeconds
import kotlinx.datetime.Instant
import java.net.URI
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

context(_: RandomGenerator, _: TimeGenerator, _: UniqueIdGenerator)
fun AcmeJwtScheme.createParameters(
    user: User = User.create(),
    access: Access = Access.create(applicationRoles = setOf(AcmeRoles.customer)),
    isUserEmailAddressVerified: Boolean = true,
    authentication: Authentication = Authentication.create(),
    token: Token = Token.create(issuedAt = (authentication.timestamp + 1.seconds).truncatedToSeconds(), expiresAt = (authentication.timestamp + 10.minutes).truncatedToSeconds()),
    session: Session = Session.create(),
    targetApplication: TargetApplication = TargetApplication.exampleDotCom,
    issuer: Issuer = Issuer.keycloakDev,
    openIdConnectParams: OpenIdConnectParams = OpenIdConnectParams.keycloakDefault,
    authorizationHeaderType: String = "Bearer"
) = AcmeJwtScheme.Parameters(user, access, isUserEmailAddressVerified, authentication, token, session, targetApplication, issuer, openIdConnectParams, authorizationHeaderType)

context(_: RandomGenerator, _: TimeGenerator, ids: UniqueIdGenerator)
fun Organization.Companion.create(id: String = ids.newId.uuid.random().stringValue, name: String = "wayneindustries") = Organization(id = id, name = name)

context(_: RandomGenerator, _: TimeGenerator, ids: UniqueIdGenerator)
fun User.Companion.create(id: String = ids.newId.uuid.random().stringValue, organization: Organization = Organization.create(), firstName: String = "Bruce", lastName: String = "Wayne", emailAddress: String = "$firstName.$lastName@${organization.name}.com", userName: String = emailAddress, otherNames: List<String> = emptyList()) = User(id = id, organization = organization, userName = userName, emailAddress = emailAddress, firstName = firstName, lastName = lastName, otherNames = otherNames)

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun Token.Companion.create(id: String = ids.newId.uuid.random().stringValue, issuedAt: Instant = (time.now() - 1.seconds).truncatedToSeconds(), expiresAt: Instant = (time.now() + 10.minutes).truncatedToSeconds()) = Token(id = id, issuedAt = issuedAt, expiresAt = expiresAt)

context(ids: UniqueIdGenerator)
fun Session.Companion.create(id: String = ids.newId.uuid.random().stringValue, stateId: String = ids.newId.uuid.random().stringValue) = Session(id = id, stateId = stateId)

val Issuer.Companion.keycloakDev: Issuer get() = Issuer(id = "https://keycloak.tools.acme.info/realms/acme".let(URI::create).let(::StringOrURI))

val OpenIdConnectParams.Companion.keycloakDefault: OpenIdConnectParams get() = OpenIdConnectParams(requestedScope = setOf("openid", "email", "profile"))

context(time: TimeGenerator)
fun Authentication.Companion.create(timestamp: Instant = (time.now() - 10.seconds).truncatedToSeconds(), isSecure: Boolean = true) = Authentication(timestamp = timestamp, isSecure = isSecure)

val TargetApplication.Companion.exampleDotCom get() = TargetApplication(audience = "account", authorizedParty = "example.web", allowedOrigins = setOf("https://app.example.dev.acme.info", "https://app.example.dev.acme.info/*"))

fun Access.Companion.create(applicationRoles: Set<String>, additionalRealmRoles: Set<String> = keycloakDefaultRealmRoles, keycloakAccountRoles: Set<String> = keycloakDefaultAccountResourceRoles) = Access(applicationRoles = applicationRoles, additionalRealmRoles = additionalRealmRoles, keycloakAccountRoles = keycloakAccountRoles)
val Access.Companion.keycloakDefaultRealmRoles: Set<String> get() = setOf("offline_access", "uma_authorization", "default-roles-apps")
val Access.Companion.keycloakDefaultAccountResourceRoles: Set<String> get() = setOf("manage-account", "manage-account-links", "view-profile")