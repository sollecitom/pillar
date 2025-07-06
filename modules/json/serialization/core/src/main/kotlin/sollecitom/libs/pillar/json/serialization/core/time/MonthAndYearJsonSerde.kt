package sollecitom.libs.pillar.json.serialization.core.time

import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.json.JSONObject
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde

private object MonthAndYearJsonSerde : JsonSerde.SchemaAware<YearMonth> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/time/MonthAndYear.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: YearMonth) = JSONObject().apply {
        put(Fields.YEAR, value.year)
        put(Fields.MONTH, value.month.name)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val year = getInt(Fields.YEAR)
        val month = getString(Fields.MONTH).let(Month::valueOf)
        YearMonth(year = year, month = month)
    }

    private object Fields {
        const val MONTH = "month"
        const val YEAR = "year"
    }
}

val YearMonth.Companion.jsonSerde: JsonSerde.SchemaAware<YearMonth> get() = MonthAndYearJsonSerde