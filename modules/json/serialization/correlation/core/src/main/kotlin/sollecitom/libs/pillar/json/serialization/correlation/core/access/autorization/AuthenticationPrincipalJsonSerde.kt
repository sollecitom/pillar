package sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization

import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object AuthenticationPrincipalJsonSerde : JsonSerde.SchemaAware<AuthorizationPrincipal> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authorization/AuthorizationPrincipal.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AuthorizationPrincipal) = JSONObject().apply {
        setValue(Fields.ROLES, value.roles, Roles.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val roles = getValue(Fields.ROLES, Roles.jsonSerde)
        AuthorizationPrincipal(roles = roles)
    }

    private object Fields {
        const val ROLES = "roles"
    }
}

val AuthorizationPrincipal.Companion.jsonSerde: JsonSerde.SchemaAware<AuthorizationPrincipal> get() = AuthenticationPrincipalJsonSerde