package sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.json.serialization.correlation.core.access.session.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.FederatedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object FederatedAuthenticationJsonSerde : JsonSerde.SchemaAware<FederatedAuthentication> {

    const val TYPE_VALUE = "federated"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authentication/FederatedAuthentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FederatedAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.TOKEN, value.token, Authentication.Token.jsonSerde)
        setValue(Fields.SESSION, value.session, FederatedSession.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = getValue(Fields.TOKEN, Authentication.Token.jsonSerde)
        val session = getValue(Fields.SESSION, FederatedSession.jsonSerde)
        FederatedAuthentication(token = token, session = session)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
        const val SESSION = "session"
    }
}

val FederatedAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedAuthentication> get() = FederatedAuthenticationJsonSerde