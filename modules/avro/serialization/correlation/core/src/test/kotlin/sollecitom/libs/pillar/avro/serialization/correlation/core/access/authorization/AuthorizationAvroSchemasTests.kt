package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AuthorizationAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = AuthorizationAvroSchemas
}