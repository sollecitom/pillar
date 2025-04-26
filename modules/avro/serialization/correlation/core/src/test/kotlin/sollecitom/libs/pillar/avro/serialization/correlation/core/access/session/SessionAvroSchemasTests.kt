package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class SessionAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = SessionAvroSchemas
}