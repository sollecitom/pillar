package sollecitom.libs.pillar.json.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object AccessContainerJsonSerde : JsonSerde.SchemaAware<AccessContainer> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/scope/AccessContainer.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AccessContainer) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        AccessContainer(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val AccessContainer.Companion.jsonSerde: JsonSerde.SchemaAware<AccessContainer> get() = AccessContainerJsonSerde