package sollecitom.libs.pillar.web.api.utils.filters.correlation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
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
import sollecitom.libs.pillar.web.api.utils.toHeaderValue
import sollecitom.libs.pillar.web.api.utils.withInvocationContext
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import sollecitom.libs.swissknife.web.api.utils.headers.of

@TestInstance(PER_CLASS)
class GatewayInfoContextParsingFilterTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val headerNames = HttpHeaderNames.of(companyName = "acme")
    private val headerName = headerNames.correlation.invocationContext

    @Test
    fun `the invocation context written by the gateway is parsed and forked`() {

        val context = InvocationContext.authenticated()
        var parsedContext: InvocationContext<*>? = null
        val handler = parsingFilter().then { request: Request ->
            parsedContext = InvocationContextKeys.key.mandatory(request)
            Response(OK)
        }

        val response = handler(Request(GET, "/").withInvocationContext(headerName, context))

        assertThat(response.status).isEqualTo(OK)
        assertThat(parsedContext!!.access).isEqualTo(context.access)
        assertThat(parsedContext.trace.parent).isEqualTo(context.trace.invocation)
    }

    @Test
    fun `the invocation context header is parsed regardless of the header name case`() {

        var parsedContext: InvocationContext<*>? = null
        val handler = parsingFilter().then { request: Request ->
            parsedContext = InvocationContextKeys.key.mandatory(request)
            Response(OK)
        }
        val response = handler(Request(GET, "/").header("X-Acme-Invocation-Context", InvocationContext.authenticated().toHeaderValue()))

        assertThat(response.status).isEqualTo(OK)
        assertThat(parsedContext).isNotNull()
    }

    @Test
    fun `a request without the invocation context header has no invocation context`() {

        var parsedContext: InvocationContext<*>? = InvocationContext.authenticated()
        val handler = parsingFilter().then { request: Request ->
            parsedContext = InvocationContextKeys.key.optional(request)
            Response(OK)
        }

        val response = handler(Request(GET, "/"))

        assertThat(response.status).isEqualTo(OK)
        assertThat(parsedContext).isNull()
    }

    @Test
    fun `multiple invocation context headers are rejected`() {

        val handler = parsingFilter().then { Response(OK) }
        val request = Request(GET, "/").withInvocationContext(headerName, InvocationContext.authenticated()).withInvocationContext(headerName, InvocationContext.authenticated())

        val response = handler(request)

        assertThat(response.status).isEqualTo(BAD_REQUEST)
    }

    @Test
    fun `a raw JSON invocation context header is rejected`() {

        val handler = parsingFilter().then { Response(OK) }
        val request = Request(GET, "/").header(headerName, InvocationContext.jsonSerde.serialize(InvocationContext.authenticated()).toString())

        val response = handler(request)

        assertThat(response.status).isEqualTo(BAD_REQUEST)
    }

    @Test
    fun `a base64url header that does not contain an invocation context is rejected`() {

        val handler = parsingFilter().then { Response(OK) }
        val request = Request(GET, "/").header(headerName, "eyJ0aGluZyI6MX0")

        val response = handler(request)

        assertThat(response.status).isEqualTo(BAD_REQUEST)
        assertThat(response.status.description).contains("The invocation context doesn't match its schema")
    }

    private fun parsingFilter() = InvocationContextFilters.parseInvocationContextFromGatewayHeader(headerNames.correlation)
}
