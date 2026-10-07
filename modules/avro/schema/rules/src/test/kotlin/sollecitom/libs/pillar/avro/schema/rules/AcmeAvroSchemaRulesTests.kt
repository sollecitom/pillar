package sollecitom.libs.pillar.avro.schema.rules

import sollecitom.libs.pillar.avro.schema.rules.specs.FieldNamesTestSpecification
import sollecitom.libs.pillar.avro.schema.rules.specs.NamespaceTestSpecification
import sollecitom.libs.pillar.avro.schema.rules.specs.SchemaNameTestSpecification
import assertk.assertThat
import org.apache.avro.Schema
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import sollecitom.libs.swissknife.avro.schema.checker.MandatoryEnumDefaultSymbolRule
import sollecitom.libs.swissknife.avro.schema.checker.NullFirstNullableUnionsRule
import sollecitom.libs.swissknife.avro.schema.checker.TopicEventUnionRule
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeAvroSchemaRulesTests {

    @Nested
    inner class SchemaName : SchemaNameTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }

    @Nested
    inner class Namespace : NamespaceTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }

    @Nested
    inner class FieldNames : FieldNamesTestSpecification {

        override val rules get() = AcmeAvroSchemaRules
    }

    @Test
    fun `nullable fields must list null first and default to null`() {

        val schema = Schema.Parser().parse("""{"type":"record","namespace":"acme.test","name":"Agent","fields":[{"name":"name","type":["null","string"]}]}""")

        val result = schema.checkAgainstRules(AcmeAvroSchemaRules)

        assertThat(result).isNotCompliantWithOnlyViolation(NullFirstNullableUnionsRule.Violation.MissingNullDefault(path = "acme.test.Agent.name"))
    }

    @Test
    fun `enums must default to UNKNOWN`() {

        val schema = Schema.Parser().parse("""{"type":"enum","namespace":"acme.test","name":"Colour","symbols":["RED"]}""")

        val result = schema.checkAgainstRules(AcmeAvroSchemaRules)

        assertThat(result).isNotCompliantWithOnlyViolation(MandatoryEnumDefaultSymbolRule.Violation(enumName = "acme.test.Colour", symbol = "UNKNOWN"))
    }

    @Test
    fun `a topic value schema must reach its events through a union`() {

        val schema = Schema.Parser().parse("""{"type":"record","namespace":"acme.test","name":"AgentEvent","fields":[{"name":"data","type":{"type":"record","name":"AgentEventData","fields":[{"name":"envelope","type":{"type":"record","name":"AgentCreated","fields":[]}}]}}]}""")

        val result = schema.checkAgainstRules(AcmeTopicValueSchemaRules)

        assertThat(result).isNotCompliantWithOnlyViolation(TopicEventUnionRule.Violation(topicSchemaName = "acme.test.AgentEvent", eventsFieldPath = listOf("data", "envelope")))
    }

    @Test
    fun `a topic value schema with an events union is compliant`() {

        val schema = Schema.Parser().parse("""{"type":"record","namespace":"acme.test","name":"AgentEvent","fields":[{"name":"data","type":{"type":"record","name":"AgentEventData","fields":[{"name":"envelope","type":[{"type":"record","name":"AgentCreated","fields":[]},{"type":"record","name":"AgentDeleted","fields":[]}]}]}}]}""")

        val result = schema.checkAgainstRules(AcmeTopicValueSchemaRules)

        assertThat(result).isCompliant()
    }
}
