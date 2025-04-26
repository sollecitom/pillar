package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OriginAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = OriginAvroSchemas
}