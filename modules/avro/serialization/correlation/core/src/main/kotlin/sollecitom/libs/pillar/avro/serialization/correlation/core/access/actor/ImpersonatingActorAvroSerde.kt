package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.Account
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import org.apache.avro.generic.GenericRecord

val ImpersonatingActor.Companion.avroSchema get() = ActorAvroSchemas.impersonatingActor
val ImpersonatingActor.Companion.avroSerde: AvroSerde<ImpersonatingActor> get() = ImpersonatingActorAvroSerde

private object ImpersonatingActorAvroSerde : AvroSerde<ImpersonatingActor> {

    override val schema get() = ImpersonatingActor.avroSchema

    override fun serialize(value: ImpersonatingActor): GenericRecord = buildRecord {

        setValue(Fields.impersonator, value.impersonator, Account.avroSerde)
        setValue(Fields.impersonated, value.impersonated, Account.avroSerde)
        setValue(Fields.authentication, value.authentication, Authentication.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val impersonator = getValue(Fields.impersonator, Account.avroSerde)
        val impersonated = getValue(Fields.impersonated, Account.avroSerde)
        val authentication = getValue(Fields.authentication, Authentication.avroSerde)
        ImpersonatingActor(impersonator = impersonator, impersonated = impersonated, authentication = authentication)
    }

    private object Fields {
        const val impersonator = "impersonator"
        const val impersonated = "impersonated"
        const val authentication = "authentication"
    }
}