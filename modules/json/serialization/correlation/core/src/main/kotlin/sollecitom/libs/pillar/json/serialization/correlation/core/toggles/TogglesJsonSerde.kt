package sollecitom.libs.pillar.json.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValues
import sollecitom.libs.swissknife.json.utils.serde.setValues
import org.json.JSONObject

private object TogglesJsonSerde : JsonSerde.SchemaAware<Toggles> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/toggles/Toggles.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Toggles) = JSONObject().apply {
        setValues(Fields.VALUES, value.values, ToggleValue.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val values = getValues(Fields.VALUES, ToggleValue.jsonSerde)
        Toggles(values = values.toSet())
    }

    private object Fields {
        const val VALUES = "values"
    }
}

val Toggles.Companion.jsonSerde: JsonSerde.SchemaAware<Toggles> get() = TogglesJsonSerde