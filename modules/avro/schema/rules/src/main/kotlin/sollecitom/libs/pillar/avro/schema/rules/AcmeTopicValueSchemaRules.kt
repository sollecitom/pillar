package sollecitom.libs.pillar.avro.schema.rules

import sollecitom.libs.swissknife.avro.schema.checker.TopicEventUnionRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import org.apache.avro.Schema

/** The [AcmeAvroSchemaRules], plus: a topic's value schema reaches its events through a union at `data.envelope`, so new event types are new union branches. */
object AcmeTopicValueSchemaRules : ComplianceRuleSet<Schema> {

    override val rules by lazy { AcmeAvroSchemaRules.rules + TopicEventUnionRule() }
}
