package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getEnvelope
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
        setEnvelope(record)
    }

    override fun deserialize(value: GenericRecord) = value.getEnvelope { branchName, envelope ->
        when (branchName) {
            DirectActor.avroSerde.schema.name -> envelope.deserializeWith(DirectActor.avroSerde)
            ActorOnBehalf.avroSerde.schema.name -> envelope.deserializeWith(ActorOnBehalf.avroSerde)
            ImpersonatingActor.avroSerde.schema.name -> envelope.deserializeWith(ImpersonatingActor.avroSerde)
            else -> error("Unknown actor type $branchName")
        }
    }
}
