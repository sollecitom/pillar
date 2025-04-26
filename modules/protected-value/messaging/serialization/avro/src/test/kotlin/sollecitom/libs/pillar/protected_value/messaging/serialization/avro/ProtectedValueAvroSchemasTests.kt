package sollecitom.libs.pillar.protected_value.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ProtectedValueAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = ProtectedValueAvroSchemas
}