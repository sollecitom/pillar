package sollecitom.libs.pillar.web.api.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import org.http4k.core.Response
import org.http4k.core.Status
import sollecitom.libs.pillar.json.serialization.web.api.jsonSerde
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import sollecitom.libs.swissknife.web.api.domain.error.ErrorCode

fun Assert<Response>.isApiError(code: String, message: String) = given { response ->

    assertThat(response.status).isEqualTo(Status.UNPROCESSABLE_ENTITY)
    val apiError = response.body(ApiError.jsonSerde)
    assertThat(apiError.code).isEqualTo(code)
    assertThat(apiError.message).isEqualTo(message)
}

fun Assert<Response>.isApiErrorWithCode(code: ErrorCode) = isApiError(code.value, code.message)