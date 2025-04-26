package sollecitom.libs.pillar.openapi.rules.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.pillar.openapi.rules.AcmeOpenApiRules
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.openapi.builder.get
import sollecitom.libs.swissknife.openapi.builder.parameters
import sollecitom.libs.swissknife.openapi.builder.post
import sollecitom.libs.swissknife.openapi.builder.put
import sollecitom.libs.swissknife.openapi.checking.checker.rules.DisallowReservedCharactersInParameterNameRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.WhitelistedAlphabetParameterNameRule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import org.junit.jupiter.api.Test

interface OperationParametersTestSpecification : OpenApiTestSpecification {

    val validParameterName: String
    val parameterLocation: String

    @Test
    fun `valid value works`() {

        val parameterName = validParameterName
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = parameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isCompliant()
    }

    @Test
    fun `cannot contain numbers`() {

        val invalidParameterName = "param-123"
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = invalidParameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation, OpenAPI> { violation ->
            assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.GET)
        }
    }

    @Test
    fun `cannot contain symbols`() {

        val invalidParameterName = "param_id"
        val api = openApi {
            path(validPath) {
                get {
                    operationId = validOperationId
                    summary = validSummary
                    parameters {
                        add {
                            name = invalidParameterName
                            `in` = parameterLocation
                            required = false
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation, OpenAPI> { violation ->
            assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.GET)
        }
    }

    @Test
    fun `cannot allow reserved URL characters`() {

        val parameterName = validParameterName
        val api = openApi {
            path(validPath) {
                put {
                    operationId = validOperationId
                    summary = validSummary
                    setValidRequestBody()
                    parameters {
                        add {
                            name = parameterName
                            `in` = parameterLocation
                            required = false
                            allowReserved = true
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(AcmeOpenApiRules)

        assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation, OpenAPI> { violation ->
            assertThat(violation.parameter.name).isEqualTo(parameterName)
            assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
            assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
            assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.PUT)
        }
    }

    interface WithDisallowedUppercaseLetters : OperationParametersTestSpecification {

        @Test
        fun `cannot contain uppercase letters`() {

            val invalidParameterName = "PARAM-ID"
            val api = openApi {
                path(validPath) {
                    post {
                        operationId = validOperationId
                        summary = validSummary
                        setValidRequestBody()
                        parameters {
                            add {
                                name = invalidParameterName
                                `in` = parameterLocation
                                required = false
                            }
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(AcmeOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation, OpenAPI> { violation ->
                assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                assertThat(violation.parameter.location.operation.method).isEqualTo(PathItem.HttpMethod.POST)
            }
        }
    }
}