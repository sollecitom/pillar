package sollecitom.libs.pillar.json.serialization.correlation.core.toggles

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object EnumToggleValueJsonSerde : JsonSerde.SchemaAware<EnumToggleValue> {

    const val TYPE_VALUE = "enum"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/toggles/EnumToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: EnumToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        val enumValue = getRequiredString(Fields.VALUE)
        EnumToggleValue(id = id, value = enumValue)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val EnumToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<EnumToggleValue> get() = EnumToggleValueJsonSerde