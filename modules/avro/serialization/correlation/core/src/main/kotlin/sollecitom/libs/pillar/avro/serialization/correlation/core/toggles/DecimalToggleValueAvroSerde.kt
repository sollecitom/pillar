package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.DecimalToggleValue
import org.apache.avro.generic.GenericRecord

val DecimalToggleValue.Companion.avroSchema get() = TogglesAvroSchemas.decimalToggleValue
val DecimalToggleValue.Companion.avroSerde: AvroSerde<DecimalToggleValue> get() = DecimalToggleValueAvroSerde

private object DecimalToggleValueAvroSerde : AvroSerde<DecimalToggleValue> {

    override val schema get() = DecimalToggleValue.avroSchema

    override fun serialize(value: DecimalToggleValue): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        set(Fields.value, value.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val decimalValue = getDouble(Fields.value)
        DecimalToggleValue(id = id, value = decimalValue)
    }

    private object Fields {
        const val id = "id"
        const val value = "value"
    }
}