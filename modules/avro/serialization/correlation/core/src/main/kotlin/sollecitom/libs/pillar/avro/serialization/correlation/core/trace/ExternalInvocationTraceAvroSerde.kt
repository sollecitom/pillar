package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import org.apache.avro.generic.GenericRecord

val ExternalInvocationTrace.Companion.avroSchema get() = TracingAvroSchemas.externalInvocationTrace
val ExternalInvocationTrace.Companion.avroSerde: AvroSerde<ExternalInvocationTrace> get() = ExternalInvocationTraceAvroSerde

private object ExternalInvocationTraceAvroSerde : AvroSerde<ExternalInvocationTrace> {

    override val schema get() = ExternalInvocationTrace.avroSchema

    override fun serialize(value: ExternalInvocationTrace): GenericRecord = buildRecord {

        setValue(Fields.invocationId, value.invocationId, Id.avroSerde)
        setValue(Fields.actionId, value.actionId, Id.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val invocationId = getValue(Fields.invocationId, Id.avroSerde)
        val actionId = getValue(Fields.actionId, Id.avroSerde)
        ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)
    }

    private object Fields {
        const val invocationId = "invocation_id"
        const val actionId = "action_id"
    }
}