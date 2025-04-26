package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class EncryptionAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = EncryptionAvroSchemas
}