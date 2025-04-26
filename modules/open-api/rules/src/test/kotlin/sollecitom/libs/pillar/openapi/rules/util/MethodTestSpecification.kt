package sollecitom.libs.pillar.openapi.rules.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.pillar.openapi.rules.AcmeOpenApiRules
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithViolation
import sollecitom.libs.swissknife.openapi.builder.content
import sollecitom.libs.swissknife.openapi.builder.operation
import sollecitom.libs.swissknife.openapi.builder.requestBody
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiFields
import sollecitom.libs.swissknife.openapi.checking.checker.rule.field.FieldRulesViolation
import sollecitom.libs.swissknife.openapi.checking.checker.rules.*
import sollecitom.libs.swissknife.openapi.checking.checker.rules.field.MandatorySuffixTextFieldRule
import sollecitom.libs.swissknife.openapi.checking.test.utils.hasSingleFieldViolation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import org.junit.jupiter.api.Test

interface MethodTestSpecification : TracingHeadersTestSpecification {

    @Test
    fun `valid value works`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isCompliant()
    }

    @Test
    fun `cannot omit operationId`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = null
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify blank operationId`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = "   "
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot omit summary`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = null
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify blank summary`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = "   "
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithViolation<MandatoryOperationFieldsRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify a description equal to the summary`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    description = summary
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<EnforceOperationDescriptionDifferentFromSummaryRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify an operationId which is not in camelCase`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    operationId = "NotCamelCase"
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<EnforceCamelCaseOperationIdRule.Violation, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
        }
    }

    @Test
    fun `cannot specify a summary that doesn't end with a full stop`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    summary = "A summary. Not ending with a full stop"
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
            assertThat(violation.field).isEqualTo(OpenApiFields.Operation.summary)
            assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                assertThat(fieldViolation.suffix).isEqualTo(".")
            }
        }
    }

    @Test
    fun `cannot specify a description that doesn't end with a full stop`() {

        val path = validPath
        val api = openApi {
            path(path) {
                operation(method) {
                    withValidFields()
                    withValidRequestBody()
                    description = "A description. Not ending with a full stop"
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>, OpenAPI> { violation ->
            assertThat(violation.operation.pathName).isEqualTo(path)
            assertThat(violation.operation.method).isEqualTo(method)
            assertThat(violation.field).isEqualTo(OpenApiFields.Operation.description)
            assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                assertThat(fieldViolation.suffix).isEqualTo(".")
            }
        }
    }

    interface WithoutRequestBody : MethodTestSpecification {

        override fun Operation.withValidRequestBody() {
            // no request body here
        }

        @Test
        fun `cannot specify request body`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<ForbiddenRequestBodyRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }
    }

    interface WithMandatoryRequestBody : MethodTestSpecification {

        override fun Operation.withValidRequestBody() {
            setValidRequestBody()
        }

        @Test
        fun `cannot omit request body`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot omit request body content media types`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            content {
                                // no media types declared here
                            }
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyContentMediaTypesRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot specify optional request body`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            required = false
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot omit request body description`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            description = null
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyDescriptionRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
            }
        }

        @Test
        fun `cannot specify a request body description that doesn't end with a full stop`() {

            val path = validPath
            val api = openApi {
                path(path) {
                    operation(method) {
                        withValidFields()
                        requestBody {
                            withValidFields()
                            description = "A request body description not ending with a full stop"
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo(path)
                assertThat(violation.operation.method).isEqualTo(method)
                assertThat(violation.field).isEqualTo(OpenApiFields.Operation.RequestBody.description)
                assertThat(violation).hasSingleFieldViolation<MandatorySuffixTextFieldRule.Violation, String> { fieldViolation ->
                    assertThat(fieldViolation.suffix).isEqualTo(".")
                }
            }
        }
    }
}