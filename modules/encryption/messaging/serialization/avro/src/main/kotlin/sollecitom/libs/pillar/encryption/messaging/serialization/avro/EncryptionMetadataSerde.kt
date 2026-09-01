package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import org.apache.avro.generic.GenericRecord

val EncryptionMode.Metadata.Companion.avroSchema get() = EncryptionAvroSchemas.encryptionMetadata
val EncryptionMode.Metadata.Companion.avroSerde: AvroSerde<EncryptionMode.Metadata> get() = EncryptionMetadataAvroSerde

/**
 * Envelope over the concrete encryption-mode metadata, so the mode a value was protected with is part of the
 * wire format rather than an assumption. Only GCM is carried today; adding a mode is a new union branch here
 * and in the schema, which leaves the encoding of the existing branches untouched.
 */
private object EncryptionMetadataAvroSerde : AvroSerde<EncryptionMode.Metadata> {

    override val schema get() = EncryptionMode.Metadata.avroSchema

    override fun serialize(value: EncryptionMode.Metadata): GenericRecord = buildRecord {
        val record = when (value) {
            is EncryptionMode.GCM.Metadata -> EncryptionMode.GCM.Metadata.avroSerde.serialize(value)
            else -> error("Unsupported encryption metadata type ${value::class.qualifiedName}")
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.gcm -> unionRecord.deserializeWith(EncryptionMode.GCM.Metadata.avroSerde)
            else -> error("Unknown encryption metadata type $unionTypeName")
        }
    }

    private fun EncryptionMode.Metadata.type(): String = when (this) {
        is EncryptionMode.GCM.Metadata -> Types.gcm
        else -> error("Unsupported encryption metadata type ${this::class.qualifiedName}")
    }

    private object Types {
        const val gcm = "gcm"
    }
}
