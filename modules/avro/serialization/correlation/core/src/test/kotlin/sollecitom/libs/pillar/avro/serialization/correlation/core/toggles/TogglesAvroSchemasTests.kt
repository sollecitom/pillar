package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TogglesAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = TogglesAvroSchemas
}