package sollecitom.libs.pillar.avro.serialization.core.currency

import sollecitom.libs.swissknife.avro.schema.catalogue.test.utils.SchemaContainerTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.core.identity.IdentityAvroSchemas

@TestInstance(PER_CLASS)
class CurrencyAvroSchemasTests : SchemaContainerTestSpecification {

    override val candidate = CurrencyAvroSchemas
}