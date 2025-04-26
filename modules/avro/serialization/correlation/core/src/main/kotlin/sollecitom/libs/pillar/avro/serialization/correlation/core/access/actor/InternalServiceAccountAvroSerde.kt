package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.ServiceAccount
import org.apache.avro.generic.GenericRecord

val ServiceAccount.Internal.Companion.avroSchema get() = ActorAvroSchemas.internalServiceAccount
val ServiceAccount.Internal.Companion.avroSerde: AvroSerde<ServiceAccount.Internal> get() = InternalServiceAccountAvroSerde

private object InternalServiceAccountAvroSerde : AvroSerde<ServiceAccount.Internal> {

    override val schema get() = ServiceAccount.Internal.avroSchema

    override fun serialize(value: ServiceAccount.Internal): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        ServiceAccount.Internal(id = id)
    }

    private object Fields {
        const val id = "id"
    }
}