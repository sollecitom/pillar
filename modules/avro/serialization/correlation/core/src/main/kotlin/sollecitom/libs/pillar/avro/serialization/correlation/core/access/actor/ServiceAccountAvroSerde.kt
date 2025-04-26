package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.ServiceAccount
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.ServiceAccount.External
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.ServiceAccount.Internal
import org.apache.avro.generic.GenericRecord

val ServiceAccount.Companion.avroSchema get() = ActorAvroSchemas.serviceAccount
val ServiceAccount.Companion.avroSerde: AvroSerde<ServiceAccount> get() = ServiceAccountAvroSerde

private object ServiceAccountAvroSerde : AvroSerde<ServiceAccount> {

    override val schema get() = ServiceAccount.avroSchema

    override fun serialize(value: ServiceAccount): GenericRecord = buildRecord {
        val record = when (value) {
            is Internal -> Internal.avroSerde.serialize(value)
            is External -> External.avroSerde.serialize(value)
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.internal -> unionRecord.deserializeWith(Internal.avroSerde)
            Types.external -> unionRecord.deserializeWith(External.avroSerde)
            else -> error("Unknown service account type $unionTypeName")
        }
    }

    private fun ServiceAccount.type(): String = when (this) {
        is Internal -> Types.internal
        is External -> Types.external
    }

    private object Types {
        const val internal = "internal"
        const val external = "external"
    }
}
