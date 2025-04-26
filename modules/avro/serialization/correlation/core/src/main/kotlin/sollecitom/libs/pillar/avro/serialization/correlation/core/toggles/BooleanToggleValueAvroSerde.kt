package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.BooleanToggleValue
import org.apache.avro.generic.GenericRecord

val BooleanToggleValue.Companion.avroSchema get() = TogglesAvroSchemas.booleanToggleValue
val BooleanToggleValue.Companion.avroSerde: AvroSerde<BooleanToggleValue> get() = BooleanToggleValueAvroSerde

private object BooleanToggleValueAvroSerde : AvroSerde<BooleanToggleValue> {

    override val schema get() = BooleanToggleValue.avroSchema

    override fun serialize(value: BooleanToggleValue): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        set(Fields.value, value.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val booleanValue = getBoolean(Fields.value)
        BooleanToggleValue(id = id, value = booleanValue)
    }

    private object Fields {
        const val id = "id"
        const val value = "value"
    }
}