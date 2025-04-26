package sollecitom.libs.pillar.json.schema.rules.specs

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.json.JSONObject
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.WhitelistedAlphabetFieldNameRule
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface PropertyNamesTestSpecification {

    val rules: ComplianceRuleSet<JsonSchema>

    val illegalCharacters
        get() = setOf(
            '_', '?', '!', '@', '>', '<', '(', ')', '[', ']', '{', '}', '!', '?', '+', '/', '*', '&', '^', '%', '$', '£', '#', '@', '|', '`', '.', ',', ';', '=', ' ', '\'', ':'
        )

    fun arguments() = parameterizedTestArguments(
        "uppercase letters" to "aCamelCaseField",
        "numbers" to "field-number-1",
        *illegalCharacters.map { "'$it' symbol" to "field$it" }.toTypedArray()
    )

    @ParameterizedTest
    @MethodSource("arguments")
    fun `property names cannot contain illegal characters`(argument: TestArgument<String>) {

        val illegalFieldName = argument.value

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": false,
                "required": [
                    "value"
                ],
                "properties": {
                    "$illegalFieldName": {
                        "type": "string"
                    },
                    "value": {
                        "type": "string",
                        "minLength": 1
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetFieldNameRule.Violation, JsonSchema> { violation ->

            assertThat(violation.property.name).isEqualTo(illegalFieldName)
        }
    }
}