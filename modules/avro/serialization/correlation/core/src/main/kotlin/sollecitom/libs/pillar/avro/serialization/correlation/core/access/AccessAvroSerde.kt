package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.Access.Authenticated
import sollecitom.libs.swissknife.correlation.core.domain.access.Access.Unauthenticated
import org.apache.avro.generic.GenericRecord

val Access.Companion.avroSchema get() = AccessAvroSchemas.access
val Access.Companion.avroSerde: AvroSerde<Access> get() = AccessAvroSerde

private object AccessAvroSerde : AvroSerde<Access> {

    override val schema get() = Access.avroSchema

    override fun serialize(value: Access): GenericRecord = buildRecord {
        val record = when (value) {
            is Authenticated -> Authenticated.avroSerde.serialize(value)
            is Unauthenticated -> Unauthenticated.avroSerde.serialize(value)
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.authenticated -> unionRecord.deserializeWith(Authenticated.avroSerde)
            Types.unauthenticated -> unionRecord.deserializeWith(Unauthenticated.avroSerde)
            else -> error("Unknown access type $unionTypeName")
        }
    }

    private fun Access.type(): String = when (this) {
        is Authenticated -> Types.authenticated
        is Unauthenticated -> Types.unauthenticated
    }

    private object Types {
        const val authenticated = "authenticated"
        const val unauthenticated = "unauthenticated"
    }
}
