package sollecitom.libs.pillar.json.schema.rules.specs

import assertk.assertThat
import assertk.assertions.containsOnly
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.DisallowRequiringUndeclaredPropertiesRule
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface RequiredPropertiesTestSpecification {

    val rules: ComplianceRuleSet<JsonSchema>

    @Test
    fun `required properties cannot contain undeclared properties`() {

        val requiredUndeclaredProperty = "undeclared"
        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": false,
                "required": [
                    "value",
                    "$requiredUndeclaredProperty"
                ],
                "properties": {
                    "value": {
                        "type": "string",
                        "minLength": 1
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<DisallowRequiringUndeclaredPropertiesRule.Violation, JsonSchema> { violation ->

            assertThat(violation.offendingProperties).containsOnly(requiredUndeclaredProperty)
        }
    }
}