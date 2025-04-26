package sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object RoleJsonSerde : JsonSerde.SchemaAware<Role> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authorization/Role.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Role) = JSONObject().apply {

        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val name = getRequiredString(Fields.NAME).let(::Name)
        Role(name = name)
    }

    private object Fields {
        const val NAME = "name"
    }
}

val Role.Companion.jsonSerde: JsonSerde.SchemaAware<Role> get() = RoleJsonSerde