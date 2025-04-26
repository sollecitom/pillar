package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.time.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

val InvocationTrace.Companion.avroSchema get() = TracingAvroSchemas.invocationTrace
val InvocationTrace.Companion.avroSerde: AvroSerde<InvocationTrace> get() = InvocationTraceAvroSerde

private object InvocationTraceAvroSerde : AvroSerde<InvocationTrace> {

    override val schema get() = InvocationTrace.avroSchema

    override fun serialize(value: InvocationTrace): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValue(Fields.createdAt, value.createdAt, Instant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val createdAt = getValue(Fields.createdAt, Instant.avroSerde)
        InvocationTrace(id = id, createdAt = createdAt)
    }

    private object Fields {
        const val id = "id"
        const val createdAt = "created_at"
    }
}