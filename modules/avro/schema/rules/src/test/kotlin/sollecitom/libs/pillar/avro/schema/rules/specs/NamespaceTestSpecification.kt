package sollecitom.libs.pillar.avro.schema.rules.specs

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import sollecitom.libs.swissknife.avro.schema.checker.MandatoryNamespacePrefixRule
import sollecitom.libs.swissknife.avro.schema.checker.WhitelistedAlphabetNamespaceNameRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal interface NamespaceTestSpecification {

    val rules: ComplianceRuleSet<Schema>

    val illegalCharacters
        get() = emptySet<Char>()

    fun arguments() = parameterizedTestArguments(
        "uppercase letters" to "aCamelCaseNamespace",
        "numbers" to "namespace_1",
        *illegalCharacters.map { "'$it' symbol" to "name${it}space" }.toTypedArray()
    )

    @ParameterizedTest
    @MethodSource("arguments")
    fun `namespace name cannot contain illegal characters`(argument: TestArgument<String>) {

        val illegalNamespaceName = "acme.${argument.value}"

        val schema = """
            {
              "type": "record",
              "namespace": "$illegalNamespaceName",
              "name": "Person",
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

        assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetNamespaceNameRule.Violation, Schema> { violation ->

            assertThat(violation.namespace).isEqualTo(illegalNamespaceName)
        }
    }

    @Test
    fun `schema name must start with common prefix`() {

        val illegalNamespacePrefix = "not.a.common.prefix.person"

        val schema = """
            {
              "type": "record",
              "namespace": "$illegalNamespacePrefix",
              "name": "Person",
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

        assertThat(result).isNotCompliantWithOnlyViolation<MandatoryNamespacePrefixRule.Violation, Schema> { violation ->

            assertThat(violation.namespace).isEqualTo(illegalNamespacePrefix)
            assertThat(violation.prefix).isEqualTo("acme.")
        }
    }
}