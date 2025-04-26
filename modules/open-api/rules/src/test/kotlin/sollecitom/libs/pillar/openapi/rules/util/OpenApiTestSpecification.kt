package sollecitom.libs.pillar.openapi.rules.util

import sollecitom.libs.swissknife.openapi.builder.OpenApiBuilder
import sollecitom.libs.swissknife.openapi.builder.content
import sollecitom.libs.swissknife.openapi.builder.mediaTypes
import sollecitom.libs.swissknife.openapi.builder.requestBody
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.RequestBody

interface OpenApiTestSpecification {

    val validPath: String
    val validOperationId: String
    val validSummary: String

    fun openApi(version: OpenApiBuilder.OpenApiVersion = OpenApiBuilder.OpenApiVersion.V3_1_0, customize: OpenApiBuilder.() -> Unit): OpenAPI

    fun Operation.withValidFields() {

        operationId = validOperationId
        summary = validSummary
    }

    fun Operation.setValidRequestBody() {

        requestBody {
            withValidFields()
        }
    }

    fun RequestBody.withValidFields() {

        required = true
        description = "Some request body description."
        content {
            mediaTypes {
                add("text/plain") {
                    schema = StringSchema()
                    example = "Some text"
                }
            }
        }
    }
}