package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import org.apache.avro.generic.GenericRecord

val EncryptionMode.XTS.Metadata.Companion.avroSchema get() = EncryptionAvroSchemas.xtsEncryptionMetadata
val EncryptionMode.XTS.Metadata.Companion.avroSerde: AvroSerde<EncryptionMode.XTS.Metadata> get() = XtsEncryptionMetadataAvroSerde

private object XtsEncryptionMetadataAvroSerde : AvroSerde<EncryptionMode.XTS.Metadata> {

    override val schema get() = EncryptionMode.XTS.Metadata.avroSchema

    override fun serialize(value: EncryptionMode.XTS.Metadata) = buildRecord {
        setByteArrayAsHexString(Fields.tweak_hex, value.tweak)
        setValue(Fields.key, value.key, CryptographicKey.Metadata.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {
        val tweak = getHexStringAsByteArray(Fields.tweak_hex)
        val key = getValue(Fields.key, CryptographicKey.Metadata.avroSerde)
        EncryptionMode.XTS.Metadata(tweak = tweak, key = key)
    }

    private object Fields {
        const val tweak_hex = "tweak_hex"
        const val key = "key"
    }
}
