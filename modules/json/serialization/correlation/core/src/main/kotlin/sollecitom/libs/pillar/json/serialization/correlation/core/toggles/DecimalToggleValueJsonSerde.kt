package sollecitom.libs.pillar.json.serialization.correlation.core.toggles

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.DecimalToggleValue
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object DecimalToggleValueJsonSerde : JsonSerde.SchemaAware<DecimalToggleValue> {

    const val TYPE_VALUE = "decimal"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/toggles/DecimalToggleValue.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DecimalToggleValue) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALUE, value.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        val decimalValue = getDouble(Fields.VALUE)
        DecimalToggleValue(id = id, value = decimalValue)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val DecimalToggleValue.Companion.jsonSerde: JsonSerde.SchemaAware<DecimalToggleValue> get() = DecimalToggleValueJsonSerde