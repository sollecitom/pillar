package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.Account
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import org.apache.avro.generic.GenericRecord

val DirectActor.Companion.avroSchema get() = ActorAvroSchemas.directActor
val DirectActor.Companion.avroSerde: AvroSerde<DirectActor> get() = DirectActorAvroSerde

private object DirectActorAvroSerde : AvroSerde<DirectActor> {

    override val schema get() = DirectActor.avroSchema

    override fun serialize(value: DirectActor): GenericRecord = buildRecord {

        setValue(Fields.account, value.account, Account.avroSerde)
        setValue(Fields.authentication, value.authentication, Authentication.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val account = getValue(Fields.account, Account.avroSerde)
        val authentication = getValue(Fields.authentication, Authentication.avroSerde)
        DirectActor(account = account, authentication = authentication)
    }

    private object Fields {
        const val account = "account"
        const val authentication = "authentication"
    }
}