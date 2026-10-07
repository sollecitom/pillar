package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getEnvelope
import sollecitom.libs.swissknife.correlation.core.domain.toggles.*
import org.apache.avro.generic.GenericRecord

val ToggleValue.Companion.avroSchema get() = TogglesAvroSchemas.toggleValue
val ToggleValue.Companion.avroSerde: AvroSerde<ToggleValue<*>> get() = ToggleValueAvroSerde

private object ToggleValueAvroSerde : AvroSerde<ToggleValue<*>> {

    override val schema get() = ToggleValue.avroSchema

    override fun serialize(value: ToggleValue<*>): GenericRecord = buildRecord {

        val record = when (value) {
            is BooleanToggleValue -> BooleanToggleValue.avroSerde.serialize(value)
            is IntegerToggleValue -> IntegerToggleValue.avroSerde.serialize(value)
            is DecimalToggleValue -> DecimalToggleValue.avroSerde.serialize(value)
            is EnumToggleValue -> EnumToggleValue.avroSerde.serialize(value)
        }
        setEnvelope(record)
    }

    override fun deserialize(value: GenericRecord) = value.getEnvelope { branchName, envelope ->
        when (branchName) {
            BooleanToggleValue.avroSerde.schema.name -> envelope.deserializeWith(BooleanToggleValue.avroSerde)
            IntegerToggleValue.avroSerde.schema.name -> envelope.deserializeWith(IntegerToggleValue.avroSerde)
            DecimalToggleValue.avroSerde.schema.name -> envelope.deserializeWith(DecimalToggleValue.avroSerde)
            EnumToggleValue.avroSerde.schema.name -> envelope.deserializeWith(EnumToggleValue.avroSerde)
            else -> error("Unknown toggle value type $branchName")
        }
    }
}