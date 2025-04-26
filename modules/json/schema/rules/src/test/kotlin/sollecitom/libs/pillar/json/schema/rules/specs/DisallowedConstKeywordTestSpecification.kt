package sollecitom.libs.pillar.json.schema.rules.specs

import assertk.assertThat
import assertk.assertions.containsOnly
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.DisallowConstKeywordRule
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface DisallowedConstKeywordTestSpecification {

    val rules: ComplianceRuleSet<JsonSchema>

    @Test
    fun `const cannot be used to restrict the value of a property`() {

        val illegalPropertyName = "value"
        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": false,
                "properties": {
                    "$illegalPropertyName": {
                        "type": "string",
                        "const": "batman"
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<DisallowConstKeywordRule.Violation, JsonSchema> { violation ->

            assertThat(violation.offendingProperties).containsOnly(illegalPropertyName)
        }
    }
}