package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import org.apache.avro.generic.GenericRecord

val Trace.Companion.avroSchema get() = TracingAvroSchemas.trace
val Trace.Companion.avroSerde: AvroSerde<Trace> get() = TraceAvroSerde

private object TraceAvroSerde : AvroSerde<Trace> {

    override val schema get() = Trace.avroSchema

    override fun serialize(value: Trace): GenericRecord = buildRecord {

        setValue(Fields.invocation, value.invocation, InvocationTrace.avroSerde)
        setValue(Fields.parent, value.parent, InvocationTrace.avroSerde)
        setValue(Fields.originating, value.originating, InvocationTrace.avroSerde)
        setValue(Fields.external, value.external, ExternalInvocationTrace.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val invocation = getValue(Fields.invocation, InvocationTrace.avroSerde)
        val parent = getValue(Fields.parent, InvocationTrace.avroSerde)
        val originating = getValue(Fields.originating, InvocationTrace.avroSerde)
        val external = getValue(Fields.external, ExternalInvocationTrace.avroSerde)
        Trace(invocation = invocation, parent = parent, originating = originating, external = external)
    }

    private object Fields {
        const val invocation = "invocation"
        const val parent = "parent"
        const val originating = "originating"
        const val external = "external"
    }
}