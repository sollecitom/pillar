package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EventAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = EventAvroSchemas
}