package sollecitom.libs.pillar.avro.serialization.test.utils

import sollecitom.libs.pillar.avro.schema.rules.AcmeAvroSchemaRules
import sollecitom.libs.swissknife.avro.serialization.test.utils.AvroSerdeTestSpecification
import sollecitom.libs.swissknife.avro.serialization.test.utils.checker.AvroSchemaComplianceTestSpecification
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface AcmeAvroSerdeTestSpecification<VALUE : Any> : AvroSerdeTestSpecification<VALUE>, AvroSchemaComplianceTestSpecification {

    override val avroSchema get() = avroSerde.schema
    override val complianceRules get() = AcmeAvroSchemaRules
}