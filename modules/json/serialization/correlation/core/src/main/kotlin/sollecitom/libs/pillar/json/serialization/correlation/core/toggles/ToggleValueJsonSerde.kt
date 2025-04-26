package sollecitom.libs.pillar.json.serialization.correlation.core.toggles

import sollecitom.libs.swissknife.correlation.core.domain.toggles.*
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object ToggleValueJsonSerde : JsonSerde.SchemaAware<ToggleValue<*>> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/toggles/ToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ToggleValue<*>) = when (value) {
        is BooleanToggleValue -> BooleanToggleValue.jsonSerde.serialize(value)
        is DecimalToggleValue -> DecimalToggleValue.jsonSerde.serialize(value)
        is IntegerToggleValue -> IntegerToggleValue.jsonSerde.serialize(value)
        is EnumToggleValue -> EnumToggleValue.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        BooleanToggleValueJsonSerde.TYPE_VALUE -> BooleanToggleValue.jsonSerde.deserialize(value)
        DecimalToggleValueJsonSerde.TYPE_VALUE -> DecimalToggleValue.jsonSerde.deserialize(value)
        IntegerToggleValueJsonSerde.TYPE_VALUE -> IntegerToggleValue.jsonSerde.deserialize(value)
        EnumToggleValueJsonSerde.TYPE_VALUE -> EnumToggleValue.jsonSerde.deserialize(value)
        else -> error("Unsupported toggle value type $type")
    }
}

private object Fields {
    const val TYPE = "type"
}

val ToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<ToggleValue<*>> get() = ToggleValueJsonSerde