package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.Account
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import org.apache.avro.generic.GenericRecord

val ActorOnBehalf.Companion.avroSchema get() = ActorAvroSchemas.actorOnBehalf
val ActorOnBehalf.Companion.avroSerde: AvroSerde<ActorOnBehalf> get() = ActorOnBehalfAvroSerde

private object ActorOnBehalfAvroSerde : AvroSerde<ActorOnBehalf> {

    override val schema get() = ActorOnBehalf.avroSchema

    override fun serialize(value: ActorOnBehalf): GenericRecord = buildRecord {

        setValue(Fields.account, value.account, Account.avroSerde)
        setValue(Fields.benefitingAccount, value.benefitingAccount, Account.avroSerde)
        setValue(Fields.authentication, value.authentication, Authentication.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val account = getValue(Fields.account, Account.avroSerde)
        val benefitingAccount = getValue(Fields.benefitingAccount, Account.avroSerde)
        val authentication = getValue(Fields.authentication, Authentication.avroSerde)
        ActorOnBehalf(account = account, benefitingAccount = benefitingAccount, authentication = authentication)
    }

    private object Fields {
        const val account = "account"
        const val benefitingAccount = "benefiting_account"
        const val authentication = "authentication"
    }
}