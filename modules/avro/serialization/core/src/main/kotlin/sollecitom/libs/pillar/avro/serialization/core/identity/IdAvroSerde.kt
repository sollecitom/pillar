package sollecitom.libs.pillar.avro.serialization.core.identity

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.libs.swissknife.core.domain.identity.*
import org.apache.avro.generic.GenericRecord

val Id.Companion.avroSchema get() = IdentityAvroSchemas.id
val Id.Companion.avroSerde: AvroSerde<Id> get() = IdAvroSerde

private object IdAvroSerde : AvroSerde<Id> {

    override val schema get() = Id.avroSchema

    override fun serialize(value: Id): GenericRecord = buildRecord {

        setEnum(Fields.type, value.type)
        set(Fields.value, value.stringValue)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val stringValue = getString(Fields.value)
        when (val type = getEnum(Fields.type)) {
            Type.STRING -> StringId(stringValue)
            Type.ULID -> ULID(stringValue)
            Type.UUID -> UUID(stringValue)
            Type.KSUID -> KSUID(stringValue)
            else -> error("Unknown Id type '${type}'")
        }
    }

    private val Id.type: String
        get() = when (this) {
            is ULID -> Type.ULID
            is KSUID -> Type.KSUID
            is UUID -> Type.UUID
            is StringId -> Type.STRING
        }

    private object Fields {
        const val type = "type"
        const val value = "value"
    }

    private object Type {
        const val ULID = "ULID"
        const val KSUID = "KSUID"
        const val UUID = "UUID"
        const val STRING = "STRING"
    }
}