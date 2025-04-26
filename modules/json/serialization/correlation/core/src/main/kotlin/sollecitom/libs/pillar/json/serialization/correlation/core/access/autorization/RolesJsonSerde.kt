package sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization

import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValues
import sollecitom.libs.swissknife.json.utils.serde.setValues
import org.json.JSONObject

private object RolesJsonSerde : JsonSerde.SchemaAware<Roles> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authorization/Roles.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Roles) = JSONObject().apply {
        setValues(Fields.VALUES, value.values, Role.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val values = getValues(Fields.VALUES, Role.jsonSerde)
        Roles(values = values.toSet())
    }

    private object Fields {
        const val VALUES = "values"
    }
}

val Roles.Companion.jsonSerde: JsonSerde.SchemaAware<Roles> get() = RolesJsonSerde