package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import org.apache.avro.generic.GenericRecord

val EnumToggleValue.Companion.avroSchema get() = TogglesAvroSchemas.enumToggleValue
val EnumToggleValue.Companion.avroSerde: AvroSerde<EnumToggleValue> get() = EnumToggleValueAvroSerde

private object EnumToggleValueAvroSerde : AvroSerde<EnumToggleValue> {

    override val schema get() = EnumToggleValue.avroSchema

    override fun serialize(value: EnumToggleValue): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        set(Fields.value, value.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val stringValue = getString(Fields.value)
        EnumToggleValue(id = id, value = stringValue)
    }

    private object Fields {
        const val id = "id"
        const val value = "value"
    }
}