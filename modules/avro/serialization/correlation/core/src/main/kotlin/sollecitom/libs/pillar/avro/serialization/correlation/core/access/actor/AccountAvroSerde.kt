package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getRecordFromUnion
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.*
import org.apache.avro.generic.GenericRecord

val Account.Companion.avroSchema get() = ActorAvroSchemas.account
val Account.Companion.avroSerde: AvroSerde<Account> get() = AccountAvroSerde

private object AccountAvroSerde : AvroSerde<Account> {

    override val schema get() = Account.avroSchema

    override fun serialize(value: Account): GenericRecord = buildRecord {
        val record = when (value) {
            is UserAccount -> UserAccount.avroSerde.serialize(value)
            is ServiceAccount -> ServiceAccount.avroSerde.serialize(value)
        }
        setRecordInUnion(value.type(), record)
    }

    override fun deserialize(value: GenericRecord) = value.getRecordFromUnion { unionTypeName, unionRecord ->
        when (unionTypeName) {
            Types.user -> unionRecord.deserializeWith(UserAccount.avroSerde)
            Types.service -> unionRecord.deserializeWith(ServiceAccount.avroSerde)
            else -> error("Unknown account type $unionTypeName")
        }
    }

    private fun Account.type(): String = when (this) {
        is UserAccount -> Types.user
        is ServiceAccount -> Types.service
    }

    private object Types {
        const val user = "user"
        const val service = "service"
    }
}
