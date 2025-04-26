package sollecitom.libs.pillar.json.serialization.correlation.core.access.session

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object SimpleSessionJsonSerde : JsonSerde.SchemaAware<SimpleSession> {

    const val TYPE_VALUE = "simple"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/session/SimpleSession.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: SimpleSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        SimpleSession(id = id)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
    }
}

val SimpleSession.Companion.jsonSerde: JsonSerde.SchemaAware<SimpleSession> get() = SimpleSessionJsonSerde