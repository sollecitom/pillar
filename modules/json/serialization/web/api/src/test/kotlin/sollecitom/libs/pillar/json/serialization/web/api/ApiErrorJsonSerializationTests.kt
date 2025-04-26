package sollecitom.libs.pillar.json.serialization.web.api

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import sollecitom.libs.swissknife.web.api.domain.error.ErrorCode
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ApiErrorJsonSerializationTests : AcmeJsonSerdeTestSpecification<ApiError> {

    override val jsonSerde get() = ApiError.jsonSerde

    override fun parameterizedArguments() = listOf(
        "generic" to ApiError(code = "a code", message = "a message"),
        "coded" to ApiError(code = ErrorCode.AuthenticatedAccessRequired)
    )
}