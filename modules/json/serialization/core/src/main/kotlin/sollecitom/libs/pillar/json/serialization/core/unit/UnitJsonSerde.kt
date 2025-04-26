package sollecitom.libs.pillar.json.serialization.core.unit

import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object UnitJsonSerde : JsonSerde.SchemaAware<Unit> {

    private const val VALUE_PLACEHOLDER = "<unit>"
    private const val SCHEMA_LOCATION = "example/http-event-bridge/result/Unit.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Unit) = JSONObject().apply {

        put(Fields.VALUE, VALUE_PLACEHOLDER)
    }

    @Suppress("RedundantUnitExpression")
    override fun deserialize(value: JSONObject): Unit = with(value) {

        val rawValue = getRequiredString(Fields.VALUE)
        check(rawValue == VALUE_PLACEHOLDER) { "Wrong type '$value'" }
        Unit
    }

    private object Fields {
        const val VALUE = "value"
    }
}

val Unit.jsonSerde: JsonSerde.SchemaAware<Unit> get() = UnitJsonSerde