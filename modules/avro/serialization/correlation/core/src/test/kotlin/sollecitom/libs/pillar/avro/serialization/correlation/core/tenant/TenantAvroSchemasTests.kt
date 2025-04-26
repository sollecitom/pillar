package sollecitom.libs.pillar.avro.serialization.correlation.core.tenant

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class TenantAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = TenantAvroSchemas
}