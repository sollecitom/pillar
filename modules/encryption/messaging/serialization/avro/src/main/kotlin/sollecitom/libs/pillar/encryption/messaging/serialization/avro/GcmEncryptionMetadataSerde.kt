package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import org.apache.avro.generic.GenericRecord

val EncryptionMode.GCM.Metadata.Companion.avroSchema get() = EncryptionAvroSchemas.gcmEncryptionMetadata
val EncryptionMode.GCM.Metadata.Companion.avroSerde: AvroSerde<EncryptionMode.GCM.Metadata> get() = GcmEncryptionMetadataAvroSerde

private object GcmEncryptionMetadataAvroSerde : AvroSerde<EncryptionMode.GCM.Metadata> {

    override val schema get() = EncryptionMode.GCM.Metadata.avroSchema

    override fun serialize(value: EncryptionMode.GCM.Metadata) = buildRecord {
        setByteArrayAsHexString(Fields.iv_hex, value.iv)
        set(Fields.authentication_tag_length_in_bits, value.authenticationTagLengthInBits)
        // Associated data is authenticated but not encrypted, so it travels in the clear and stays optional.
        setByteArrayAsHexString(Fields.associated_data_hex, value.associatedData)
        setValue(Fields.key, value.key, CryptographicKey.Metadata.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {
        val iv = getHexStringAsByteArray(Fields.iv_hex)
        val authenticationTagLengthInBits = getInt(Fields.authentication_tag_length_in_bits)
        val associatedData = getHexStringAsByteArrayOrNull(Fields.associated_data_hex)
        val key = getValue(Fields.key, CryptographicKey.Metadata.avroSerde)
        EncryptionMode.GCM.Metadata(iv = iv, authenticationTagLengthInBits = authenticationTagLengthInBits, associatedData = associatedData, key = key)
    }

    private object Fields {
        const val iv_hex = "iv_hex"
        const val authentication_tag_length_in_bits = "authentication_tag_length_in_bits"
        const val associated_data_hex = "associated_data_hex"
        const val key = "key"
    }
}
