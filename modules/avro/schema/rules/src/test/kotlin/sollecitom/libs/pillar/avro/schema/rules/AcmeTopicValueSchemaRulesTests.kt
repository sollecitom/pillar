package sollecitom.libs.pillar.avro.schema.rules

import assertk.assertThat
import org.apache.avro.Schema
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.avro.schema.checker.TopicEventUnionRule
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation

@TestInstance(PER_CLASS)
class AcmeTopicValueSchemaRulesTests {

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
