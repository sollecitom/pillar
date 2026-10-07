package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeWith
import sollecitom.libs.swissknife.avro.serialization.utils.getEnvelope
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
        setEnvelope(record)
    }

    override fun deserialize(value: GenericRecord) = value.getEnvelope { branchName, envelope ->
        when (branchName) {
            Authenticated.avroSerde.schema.name -> envelope.deserializeWith(Authenticated.avroSerde)
            Unauthenticated.avroSerde.schema.name -> envelope.deserializeWith(Unauthenticated.avroSerde)
            else -> error("Unknown access type $branchName")
        }
    }
}
