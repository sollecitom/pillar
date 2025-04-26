package sollecitom.libs.pillar.protected_value.messaging.serialization.avro

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.pillar.encryption.messaging.serialization.avro.EncryptionAvroSchemas
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import org.apache.avro.Schema

object ProtectedValueAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.protected.value") {

    val protectedString: Schema by lazy { getSchema(name = "ProtectedString", dependencies = setOf(Id.avroSchema, EncryptionAvroSchemas.ctrEncryptionMetadata)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(protectedString)
}