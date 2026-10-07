package sollecitom.libs.pillar.avro.schema.rules

import sollecitom.libs.swissknife.avro.schema.checker.TopicEventUnionRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import org.apache.avro.Schema

object AcmeTopicValueSchemaRules : ComplianceRuleSet<Schema> {

    override val rules by lazy { AcmeAvroSchemaRules.rules + TopicEventUnionRule() }
}
