package sollecitom.libs.pillar.json.serialization.correlation.core.trace

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import kotlinx.datetime.Instant
import org.json.JSONObject

private object InvocationTraceJsonSerde : JsonSerde.SchemaAware<InvocationTrace> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/trace/InvocationTrace.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InvocationTrace) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.CREATED_AT, value.createdAt.toString())
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        val createdAt = getRequiredString(Fields.CREATED_AT).let(Instant::parse)
        InvocationTrace(id = id, createdAt = createdAt)
    }

    private object Fields {
        const val ID = "id"
        const val CREATED_AT = "created-at"
    }
}

val InvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationTrace> get() = InvocationTraceJsonSerde