package sollecitom.libs.pillar.avro.schema.rules.specs

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import sollecitom.libs.swissknife.avro.schema.checker.UppercaseSchemaNameRule
import sollecitom.libs.swissknife.avro.schema.checker.WhitelistedAlphabetSchemaNameRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface SchemaNameTestSpecification {

    val rules: ComplianceRuleSet<Schema>

    fun arguments() = parameterizedTestArguments( "numbers" to "Schema_1")

    @ParameterizedTest
    @MethodSource("arguments")
    fun `schema name cannot contain illegal characters`(argument: TestArgument<String>) {

        val illegalSchemaName = argument.value

        val schema = """
            {
              "type": "record",
              "namespace": "acme.test",
              "name": "$illegalSchemaName",
              "fields": [
                {
                  "name": "value",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "name": "name",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "name": "version",
                  "type": [
                    "null",
                    "string"
                  ]
                }
              ]
            }
        """.trimIndent().let<String, Schema>(Schema.Parser()::parse)

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetSchemaNameRule.Violation, Schema> { violation ->

            assertThat(violation.schemaName).isEqualTo(illegalSchemaName)
        }
    }

    @Test
    fun `schema name must start with an uppercase letter`() {

        val illegalSchemaName = "lowercaseFirstLetter"

        val schema = """
            {
              "type": "record",
              "namespace": "acme.test",
              "name": "$illegalSchemaName",
              "fields": [
                {
                  "name": "value",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "name": "name",
                  "type": [
                    "null",
                    "string"
                  ]
                },
                {
                  "name": "version",
                  "type": [
                    "null",
                    "string"
                  ]
                }
              ]
            }
        """.trimIndent().let<String, Schema>(Schema.Parser()::parse)

        val result = schema.checkAgainstRules(rules)

        assertThat(result).isNotCompliantWithOnlyViolation<UppercaseSchemaNameRule.Violation, Schema> { violation ->

            assertThat(violation.schemaName).isEqualTo(illegalSchemaName)
        }
    }
}