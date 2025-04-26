package sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication

import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.CredentialsBasedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.FederatedAuthentication
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.StatelessAuthentication
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object AuthenticationJsonSerde : JsonSerde.SchemaAware<Authentication> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authentication/Authentication.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Authentication) = when (value) {
        is CredentialsBasedAuthentication -> CredentialsBasedAuthentication.jsonSerde.serialize(value)
        is FederatedAuthentication -> FederatedAuthentication.jsonSerde.serialize(value)
        is StatelessAuthentication -> StatelessAuthentication.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        CredentialsBasedAuthenticationJsonSerde.TYPE_VALUE -> CredentialsBasedAuthenticationJsonSerde.deserialize(value)
        FederatedAuthenticationJsonSerde.TYPE_VALUE -> FederatedAuthenticationJsonSerde.deserialize(value)
        StatelessAuthenticationJsonSerde.TYPE_VALUE -> StatelessAuthenticationJsonSerde.deserialize(value)
        else -> error("Unsupported authentication type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Authentication.Companion.jsonSerde: JsonSerde.SchemaAware<Authentication> get() = AuthenticationJsonSerde