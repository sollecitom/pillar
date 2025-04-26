package sollecitom.libs.pillar.avro.schema.rules.specs

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import sollecitom.libs.swissknife.avro.schema.checker.WhitelistedAlphabetFieldNameRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface FieldNamesTestSpecification {

    val rules: ComplianceRuleSet<Schema>

    fun arguments() = parameterizedTestArguments(
        "uppercase letters" to "aCamelCaseField", "numbers" to "field_number_1"
    )

    @ParameterizedTest
    @MethodSource("arguments")
    fun `field names cannot contain illegal characters`(argument: TestArgument<String>) {

        val illegalFieldName = argument.value

        val schema = """
            {
              "type": "record",
              "namespace": "acme.test",
              "name": "Agent",
              "fields": [
                {
                  "name": "$illegalFieldName",
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

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetFieldNameRule.Violation, Schema> { violation ->

            assertThat(violation.field.name()).isEqualTo(illegalFieldName)
        }
    }
}