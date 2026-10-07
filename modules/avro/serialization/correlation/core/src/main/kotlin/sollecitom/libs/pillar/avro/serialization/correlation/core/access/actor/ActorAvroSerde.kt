package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import org.apache.avro.generic.GenericRecord

val Actor.Companion.avroSchema get() = ActorAvroSchemas.actor
val Actor.Companion.avroSerde: AvroSerde<Actor> get() = ActorAvroSerde

private object ActorAvroSerde : AvroSerde<Actor> {

    override val schema get() = Actor.avroSchema

    override fun serialize(value: Actor): GenericRecord = buildRecord {
        val record = when (value) {
            is DirectActor -> DirectActor.avroSerde.serialize(value)
            is ActorOnBehalf -> ActorOnBehalf.avroSerde.serialize(value)
            is ImpersonatingActor -> ImpersonatingActor.avroSerde.serialize(value)
        }
        setRecordInUnion(record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { branchName, unionRecord ->
        when (branchName) {
            Types.direct -> unionRecord.deserializeWith(DirectActor.avroSerde)
            Types.onBehalf -> unionRecord.deserializeWith(ActorOnBehalf.avroSerde)
            Types.impersonating -> unionRecord.deserializeWith(ImpersonatingActor.avroSerde)
            else -> error("Unknown actor type $branchName")
        }
    }


    private object Types {
        const val direct = "DirectActor"
        const val onBehalf = "ActorOnBehalf"
        const val impersonating = "ImpersonatingActor"
    }
}
