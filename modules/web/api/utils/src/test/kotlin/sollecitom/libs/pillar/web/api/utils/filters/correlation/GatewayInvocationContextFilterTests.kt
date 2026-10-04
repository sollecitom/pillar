package sollecitom.libs.pillar.web.api.utils.filters.correlation

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessorConfiguration
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import sollecitom.libs.swissknife.web.api.utils.headers.of

@TestInstance(PER_CLASS)
class GatewayInvocationContextFilterTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val headerNames = HttpHeaderNames.of(companyName = "acme")

    @Test
    fun `a client-supplied invocation context header is replaced by the one built by the gateway`() {

        val forgedContext = InvocationContext.authenticated()
        var forwardedRequest: Request? = null
        var parsedContext: InvocationContext<*>? = null
        val gateway = gatewayFilters().then { request: Request ->
            forwardedRequest = request
            parsedContext = InvocationContextFilter.key.mandatory(request)
            Response(OK)
        }

        val response = gateway(unauthenticatedRequest().header("x-acme-invocation-context", forgedContext.serialized()))

        assertThat(response.status).isEqualTo(OK)
        assertThat(forwardedRequest!!.headerValues("x-acme-invocation-context")).hasSize(1)
        assertThat(parsedContext!!.access).isInstanceOf<Access.Unauthenticated>()
    }

    @Test
    fun `a client-supplied invocation context header is replaced regardless of the header name case`() {

        val forgedContext = InvocationContext.authenticated()
        var parsedContext: InvocationContext<*>? = null
        val gateway = gatewayFilters().then { request: Request ->
            parsedContext = InvocationContextFilter.key.mandatory(request)
            Response(OK)
        }

        val response = gateway(unauthenticatedRequest().header("X-Acme-Invocation-Context", forgedContext.serialized()))

        assertThat(response.status).isEqualTo(OK)
        assertThat(parsedContext!!.access).isInstanceOf<Access.Unauthenticated>()
    }

    @Test
    fun `multiple invocation context headers are rejected`() {

        val handler = InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames.correlation).then(okHandler)
        val request = Request(GET, "/").header("x-acme-invocation-context", InvocationContext.authenticated().serialized()).header("x-acme-invocation-context", InvocationContext.authenticated().serialized())

        val response = handler(request)

        assertThat(response.status).isEqualTo(BAD_REQUEST)
    }

    @Test
    fun `the invocation context header is parsed regardless of the header name case`() {

        val context = InvocationContext.authenticated()
        var parsedContext: InvocationContext<*>? = null
        val handler = InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames.correlation).then { request: Request ->
            parsedContext = InvocationContextFilter.key.mandatory(request)
            Response(OK)
        }

        val response = handler(Request(GET, "/").header("X-Acme-Invocation-Context", context.serialized()))

        assertThat(response.status).isEqualTo(OK)
        assertThat(parsedContext!!.access).isInstanceOf<Access.Authenticated>()
    }

    private val okHandler: HttpHandler = { Response(OK) }

    private fun gatewayFilters() = InvocationContextFilter.parseInvocationContextFromRequest(headerNames = headerNames, issuerForDomain = { error("No JWT expected") }, jwtProcessorConfiguration = newJwtProcessorConfiguration(), randomGenerator = this, timeGenerator = this, uniqueIdGenerator = this).then(InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames.correlation))

    private fun unauthenticatedRequest() = Request(GET, "/").header("x-acme-external-trace-invocation-id", newId().stringValue).header("x-acme-external-trace-action-id", newId().stringValue)

    private fun InvocationContext<*>.serialized() = InvocationContext.jsonSerde.serialize(this).toString()
}
