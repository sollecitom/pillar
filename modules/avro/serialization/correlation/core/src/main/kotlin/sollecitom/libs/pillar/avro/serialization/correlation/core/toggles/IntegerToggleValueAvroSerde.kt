package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.IntegerToggleValue
import org.apache.avro.generic.GenericRecord

val IntegerToggleValue.Companion.avroSchema get() = TogglesAvroSchemas.integerToggleValue
val IntegerToggleValue.Companion.avroSerde: AvroSerde<IntegerToggleValue> get() = IntegerToggleValueAvroSerde

private object IntegerToggleValueAvroSerde : AvroSerde<IntegerToggleValue> {

    override val schema get() = IntegerToggleValue.avroSchema

    override fun serialize(value: IntegerToggleValue): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        set(Fields.value, value.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val integerValue = getLong(Fields.value)
        IntegerToggleValue(id = id, value = integerValue)
    }

    private object Fields {
        const val id = "id"
        const val value = "value"
    }
}