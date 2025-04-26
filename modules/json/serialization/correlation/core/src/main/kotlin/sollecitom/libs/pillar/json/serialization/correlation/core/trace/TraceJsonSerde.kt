package sollecitom.libs.pillar.json.serialization.correlation.core.trace

import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object TraceJsonSerde : JsonSerde.SchemaAware<Trace> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/trace/Trace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Trace) = JSONObject().apply {
        setValue(Fields.INVOCATION, value.invocation, InvocationTrace.jsonSerde)
        setValue(Fields.PARENT, value.parent, InvocationTrace.jsonSerde)
        setValue(Fields.ORIGINATING, value.originating, InvocationTrace.jsonSerde)
        setValue(Fields.EXTERNAL, value.external, ExternalInvocationTrace.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val invocation = getValue(Fields.INVOCATION, InvocationTrace.jsonSerde)
        val parent = getValue(Fields.PARENT, InvocationTrace.jsonSerde)
        val originating = getValue(Fields.ORIGINATING, InvocationTrace.jsonSerde)
        val external = getValue(Fields.EXTERNAL, ExternalInvocationTrace.jsonSerde)
        Trace(invocation = invocation, parent = parent, originating = originating, external = external)
    }

    private object Fields {
        const val INVOCATION = "invocation"
        const val PARENT = "parent"
        const val ORIGINATING = "originating"
        const val EXTERNAL = "external"
    }
}

val Trace.Companion.jsonSerde: JsonSerde.SchemaAware<Trace> get() = TraceJsonSerde