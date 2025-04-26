package sollecitom.libs.pillar.json.serialization.correlation.core.trace

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object ExternalInvocationTraceJsonSerde : JsonSerde.SchemaAware<ExternalInvocationTrace> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/trace/ExternalInvocationTrace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ExternalInvocationTrace) = JSONObject().apply {
        setValue(Fields.INVOCATION_ID, value.invocationId, Id.jsonSerde)
        setValue(Fields.ACTION_ID, value.actionId, Id.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val invocationId = getValue(Fields.INVOCATION_ID, Id.jsonSerde)
        val actionId = getValue(Fields.ACTION_ID, Id.jsonSerde)
        ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)
    }

    private object Fields {
        const val INVOCATION_ID = "invocation-id"
        const val ACTION_ID = "action-id"
    }
}

val ExternalInvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<ExternalInvocationTrace> get() = ExternalInvocationTraceJsonSerde