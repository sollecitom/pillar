package sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication

import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.StatelessAuthentication
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object StatelessAuthenticationJsonSerde : JsonSerde.SchemaAware<StatelessAuthentication> {

    const val TYPE_VALUE = "stateless"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authentication/StatelessAuthentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: StatelessAuthentication) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.TOKEN, value.token, Authentication.Token.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val token = getValue(Fields.TOKEN, Authentication.Token.jsonSerde)
        StatelessAuthentication(token = token)
    }

    private object Fields {
        const val TYPE = "type"
        const val TOKEN = "token"
    }
}

val StatelessAuthentication.Companion.jsonSerde: JsonSerde.SchemaAware<StatelessAuthentication> get() = StatelessAuthenticationJsonSerde