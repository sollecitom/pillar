package sollecitom.libs.pillar.web.api.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.json.JSONObject
import org.junit.jupiter.api.Test
import sollecitom.libs.pillar.web.api.utils.api.withInvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.http4k.utils.contentLength
import sollecitom.libs.swissknife.http4k.utils.contentType
import sollecitom.libs.swissknife.openapi.validation.request.validator.ValidationReportError
import sollecitom.libs.swissknife.web.api.domain.error.ErrorCode
import sollecitom.libs.swissknife.web.api.test.utils.EndpointTestSpecification
import sollecitom.libs.swissknife.web.api.utils.api.HttpDrivingAdapter

interface ErrorsEndpointHttpTestSpecification : EndpointTestSpecification {

    val invalidVersion: String get() = "!"
    val unsupportedContentType: ContentType get() = ContentType.Companion.TEXT_PLAIN
    fun validRequestPayload(): JSONObject
    fun apiWithValidResponse(): HttpDrivingAdapter
    fun validInvocationContext(): InvocationContext<*>
    fun invalidInvocationContext(): InvocationContext<*>

    @Test
    fun `attempting to perform the invocation with invalid access`() {

        val api = apiWithValidResponse()
        val json = validRequestPayload()
        val invocationContext = invalidInvocationContext()
        val request = Request.Companion(Method.POST, path(path)).body(json).withInvocationContext(invocationContext)
        request.ensureCompliantWithOpenApi()

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response).isApiErrorWithCode(code = ErrorCode.AuthenticatedAccessRequired)
    }

    @Test
    fun `attempting to use an invalid content type`() {

        val api = apiWithValidResponse()
        val json = validRequestPayload()
        val invocationContext = validInvocationContext()
        val request = Request.Companion(Method.POST, path(path)).body(json).contentType(unsupportedContentType).contentLength(json.toString().length).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.ContentTypeNotAllowed)

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.Companion.UNSUPPORTED_MEDIA_TYPE)
    }

    @Test
    fun `attempting to send malformed JSON in the request body`() {

        val api = apiWithValidResponse()
        val invalidJson = "a{ \"b\": \"c\"}"
        val invocationContext = validInvocationContext()
        val request = Request.Companion(Method.POST, path(path)).body(invalidJson).contentType(ContentType.Companion.APPLICATION_JSON).contentLength(invalidJson.length).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.InvalidJson)

        val response = api(request)

        assertThat(response).compliesWithOpenApiForRequest(request)
        assertThat(response.status).isEqualTo(Status.Companion.BAD_REQUEST)
    }

    @Test
    fun `attempting to submit an instruction with an invalid version`() {

        val api = apiWithValidResponse()
        val json = validRequestPayload()
        val invocationContext = validInvocationContext()
        val request = Request.Companion(Method.POST, path(pathWithVersion(invalidVersion))).body(json).withInvocationContext(invocationContext)
        request.ensureNonCompliantWithOpenApi(error = ValidationReportError.Request.UnknownPath)

        val response = api(request)

        assertThat(response).doesNotComplyWithOpenApiForRequest(request, ValidationReportError.Request.UnknownPath)
        assertThat(response.status).isEqualTo(Status.Companion.NOT_FOUND)
    }
}