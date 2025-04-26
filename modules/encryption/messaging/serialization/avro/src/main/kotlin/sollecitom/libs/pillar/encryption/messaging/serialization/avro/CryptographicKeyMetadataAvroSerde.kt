package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getLong
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import org.apache.avro.generic.GenericRecord

val CryptographicKey.Metadata.Companion.avroSchema get() = EncryptionAvroSchemas.cryptographicKeyMetadata
val CryptographicKey.Metadata.Companion.avroSerde: AvroSerde<CryptographicKey.Metadata> get() = CryptographicKeyMetadataAvroSerde

private object CryptographicKeyMetadataAvroSerde : AvroSerde<CryptographicKey.Metadata> {

    override val schema get() = CryptographicKey.Metadata.avroSchema

    override fun serialize(value: CryptographicKey.Metadata) = buildRecord {
        set(Fields.algorithm, value.algorithm)
        set(Fields.format, value.format)
        set(Fields.hash, value.hash)
    }

    override fun deserialize(value: GenericRecord) = with(value) {
        val algorithm = getString(Fields.algorithm)
        val format = getString(Fields.format)
        val hash = getLong(Fields.hash)
        CryptographicKey.Metadata(algorithm, format, hash)
    }

    private object Fields {

        const val algorithm = "algorithm"
        const val format = "format"
        const val hash = "hash"
    }
}