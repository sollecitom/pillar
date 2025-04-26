package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import org.apache.avro.generic.GenericRecord

val EncryptionMode.CTR.Metadata.Companion.avroSchema get() = EncryptionAvroSchemas.ctrEncryptionMetadata
val EncryptionMode.CTR.Metadata.Companion.avroSerde: AvroSerde<EncryptionMode.CTR.Metadata> get() = CtrEncryptionMetadataAvroSerde

private object CtrEncryptionMetadataAvroSerde : AvroSerde<EncryptionMode.CTR.Metadata> {

    override val schema get() = EncryptionMode.CTR.Metadata.avroSchema

    override fun serialize(value: EncryptionMode.CTR.Metadata) = buildRecord {
        setByteArrayAsHexString(Fields.iv_hex, value.iv)
        setValue(Fields.key, value.key, CryptographicKey.Metadata.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {
        val iv = getHexStringAsByteArray(Fields.iv_hex)
        val key = getValue(Fields.key, CryptographicKey.Metadata.avroSerde)
        EncryptionMode.CTR.Metadata(iv = iv, key = key)
    }

    private object Fields {
        const val iv_hex = "iv_hex"
        const val key = "key"
    }
}