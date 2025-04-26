package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TracingAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = TracingAvroSchemas
}