package sollecitom.libs.pillar.json.serialization.correlation.core.access.session

import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.correlation.core.domain.access.session.Session
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object SessionJsonSerde : JsonSerde.SchemaAware<Session> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/session/Session.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Session) = when (value) {
        is SimpleSession -> SimpleSession.jsonSerde.serialize(value)
        is FederatedSession -> FederatedSession.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        SimpleSessionJsonSerde.TYPE_VALUE -> SimpleSession.jsonSerde.deserialize(value)
        FederatedSessionJsonSerde.TYPE_VALUE -> FederatedSession.jsonSerde.deserialize(value)
        else -> error("Unsupported session type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Session.Companion.jsonSerde: JsonSerde.SchemaAware<Session> get() = SessionJsonSerde