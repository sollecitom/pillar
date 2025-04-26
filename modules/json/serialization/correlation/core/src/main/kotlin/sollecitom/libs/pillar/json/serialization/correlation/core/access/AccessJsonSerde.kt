package sollecitom.libs.pillar.json.serialization.correlation.core.access

import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object AccessJsonSerde : JsonSerde.SchemaAware<Access> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/Access.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access) = when (value) {
        is Access.Authenticated -> Access.Authenticated.jsonSerde.serialize(value)
        is Access.Unauthenticated -> Access.Unauthenticated.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        AuthenticatedAccessJsonSerde.TYPE_VALUE -> Access.Authenticated.jsonSerde.deserialize(value)
        UnauthenticatedAccessJsonSerde.TYPE_VALUE -> Access.Unauthenticated.jsonSerde.deserialize(value)
        else -> error("Unsupported access type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Access.Companion.jsonSerde: JsonSerde.SchemaAware<Access> get() = AccessJsonSerde