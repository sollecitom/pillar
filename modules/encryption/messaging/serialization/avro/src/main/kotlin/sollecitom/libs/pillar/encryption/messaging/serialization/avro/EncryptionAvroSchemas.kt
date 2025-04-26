package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object EncryptionAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.encryption") {

    val cryptographicKeyMetadata: Schema by lazy { getSchema(name = "CryptographicKeyMetadata") }
    val ctrEncryptionMetadata: Schema by lazy { getSchema(name = "CtrEncryptionMetadata", dependencies = setOf(cryptographicKeyMetadata)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(cryptographicKeyMetadata, ctrEncryptionMetadata)
}