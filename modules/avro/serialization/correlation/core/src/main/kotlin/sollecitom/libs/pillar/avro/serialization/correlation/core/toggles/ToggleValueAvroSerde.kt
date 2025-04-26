package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
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
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.boolean -> unionRecord.deserializeWith(BooleanToggleValue.avroSerde)
            Types.integer -> unionRecord.deserializeWith(IntegerToggleValue.avroSerde)
            Types.decimal -> unionRecord.deserializeWith(DecimalToggleValue.avroSerde)
            Types.enum -> unionRecord.deserializeWith(EnumToggleValue.avroSerde)
            else -> error("Unknown toggle value type $unionTypeName")
        }
    }

    private fun ToggleValue<*>.type(): String = when (this) {
        is BooleanToggleValue -> Types.boolean
        is IntegerToggleValue -> Types.integer
        is DecimalToggleValue -> Types.decimal
        is EnumToggleValue -> Types.enum
    }

    private object Types {
        const val boolean = "BooleanToggleValue"
        const val integer = "IntegerToggleValue"
        const val decimal = "DecimalToggleValue"
        const val enum = "EnumToggleValue"
    }
}