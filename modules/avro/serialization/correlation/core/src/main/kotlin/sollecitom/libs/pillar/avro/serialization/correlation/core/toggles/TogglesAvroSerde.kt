package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValues
import sollecitom.libs.swissknife.avro.serialization.utils.setValues
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import org.apache.avro.generic.GenericRecord

val Toggles.Companion.avroSchema get() = TogglesAvroSchemas.toggles
val Toggles.Companion.avroSerde: AvroSerde<Toggles> get() = TogglesAvroSerde

private object TogglesAvroSerde : AvroSerde<Toggles> {

    override val schema get() = Toggles.avroSchema

    override fun serialize(value: Toggles): GenericRecord = buildRecord {

        setValues(Fields.values, value.values.toList(), ToggleValue.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val values = getValues(Fields.values, ToggleValue.avroSerde).toSet()
        Toggles(values = values)
    }

    private object Fields {
        const val values = "values"
    }
}