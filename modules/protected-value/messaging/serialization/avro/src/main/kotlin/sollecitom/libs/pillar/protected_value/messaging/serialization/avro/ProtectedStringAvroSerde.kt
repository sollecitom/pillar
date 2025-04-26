package sollecitom.libs.pillar.protected_value.messaging.serialization.avro

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.encryption.messaging.serialization.avro.avroSerde
import sollecitom.libs.pillar.protected_value.domain.ProtectedString
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueData
import org.apache.avro.generic.GenericRecord

val ProtectedValue.Companion.stringAvroSchema get() = ProtectedValueAvroSchemas.protectedString
val ProtectedValue.Companion.stringAvroSerde: AvroSerde<ProtectedString> get() = ProtectedStringAvroSerde

private object ProtectedStringAvroSerde : AvroSerde<ProtectedString> {

    override val schema get() = ProtectedValue.stringAvroSchema

    override fun serialize(value: ProtectedString) = buildRecord {
        set(Fields.name, value.name.value)
        setValue(Fields.owner, value.owner, Id.avroSerde)
        setByteArrayAsHexString(Fields.value_hex, value.value)
        setValue(Fields.metadata, value.metadata, EncryptionMode.CTR.Metadata.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {
        val name = getString(Fields.name).let(::Name)
        val owner = getValue(Fields.owner, Id.avroSerde)
        val bytesValue = getHexStringAsByteArray(Fields.value_hex)
        val metadata = getValue(Fields.metadata, EncryptionMode.CTR.Metadata.avroSerde)
        ProtectedValueData<String, EncryptionMode.CTR.Metadata>(bytesValue, name, owner, metadata)
    }

    private object Fields {
        const val name = "name"
        const val owner = "owner"
        const val value_hex = "value_hex"
        const val metadata = "metadata"
    }
}