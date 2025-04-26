package sollecitom.libs.pillar.json.schema.rules.specs

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliantWith
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.EnforcedAdditionalPropertiesValueRule
import sollecitom.libs.swissknife.json.utils.checker.rules.MandatoryAdditionalPropertiesRule
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface AdditionalPropertiesTestSpecification {

    val rules: ComplianceRuleSet<JsonSchema>

    @Test
    fun `additionalProperties cannot be set to true`() {

        val illegalAdditionalPropertiesValue = true

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": $illegalAdditionalPropertiesValue,
                "properties": {
                    "value": {
                        "type": "string",
                        "minLength": 1
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<EnforcedAdditionalPropertiesValueRule.Violation, JsonSchema> { violation ->

            assertThat(violation.value).isEqualTo(false)
        }
    }

    @Test
    fun `additionalProperties can be set to false`() {

        val legalAdditionalPropertiesValue = false

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": $legalAdditionalPropertiesValue,
                "properties": {
                    "value": {
                        "type": "string",
                        "minLength": 1
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        assertThat(schema).isCompliantWith(rules)
    }

    @Test
    fun `additionalProperties cannot be omitted`() {

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "properties": {
                    "value": {
                        "type": "string",
                        "minLength": 1
                    }
                }
        }
        """.trimIndent().let(::JSONObject).asSchema()

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation(MandatoryAdditionalPropertiesRule.Violation)
    }

    @Test
    fun `additionalProperties can be omitted in pure union types`() {

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "oneOf": [
                    {
                        "type": "object",
                        "additionalProperties": false,
                        "properties": {
                            "name": {
                                "type": "string",
                                "minLength": 1
                             }
                        }
                    },
                    {
                        "type": "object",
                        "additionalProperties": false,
                        "properties": {
                            "value": {
                                "type": "string",
                                "minLength": 1
                             }
                        }
                    }
                ]
            }
        """.trimIndent().let(::JSONObject).asSchema()

        assertThat(schema).isCompliantWith(rules)
    }

    @Test
    fun `additionalProperties can set to true in pure union types`() {

        val schema = """
            {
                "${"$"}schema": "https://json-schema.org/draft/2020-12/schema",
                "type": "object",
                "additionalProperties": true,
                "oneOf": [
                    {
                        "type": "object",
                        "additionalProperties": false,
                        "properties": {
                            "name": {
                                "type": "string",
                                "minLength": 1
                             }
                        }
                    },
                    {
                        "type": "object",
                        "additionalProperties": false,
                        "properties": {
                            "value": {
                                "type": "string",
                                "minLength": 1
                             }
                        }
                    }
                ]
            }
        """.trimIndent().let(::JSONObject).asSchema()

        assertThat(schema).isCompliantWith(rules)
    }
}