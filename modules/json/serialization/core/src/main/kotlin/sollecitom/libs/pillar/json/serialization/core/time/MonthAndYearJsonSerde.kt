package sollecitom.libs.pillar.json.serialization.core.time

import sollecitom.libs.swissknife.core.domain.time.MonthAndYear
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject
import java.time.Month
import java.time.Year

private object MonthAndYearJsonSerde : JsonSerde.SchemaAware<MonthAndYear> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/time/MonthAndYear.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: MonthAndYear) = JSONObject().apply {
        put(Fields.MONTH, value.month.name)
        put(Fields.YEAR, value.year.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val month = getString(Fields.MONTH)
        val year = getInt(Fields.YEAR)
        MonthAndYear(Month.valueOf(month), Year.of(year))
    }

    private object Fields {
        const val MONTH = "month"
        const val YEAR = "year"
    }
}

val MonthAndYear.Companion.jsonSerde: JsonSerde.SchemaAware<MonthAndYear> get() = MonthAndYearJsonSerde