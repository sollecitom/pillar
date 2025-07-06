package sollecitom.libs.pillar.avro.serialization.core.time

import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum
import sollecitom.libs.swissknife.avro.serialization.utils.getInt

val YearMonth.Companion.avroSchema get() = TimeAvroSchemas.monthAndYear

val YearMonth.Companion.avroSerde: AvroSerde<YearMonth> get() = MonthAndYearAvroSerde

private object MonthAndYearAvroSerde : AvroSerde<YearMonth> {

    override val schema get() = YearMonth.avroSchema

    override fun serialize(value: YearMonth): GenericRecord = buildRecord {

        set(Fields.YEAR, value.year)
        setEnum(Fields.MONTH, value.month.name)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val year = getInt(Fields.YEAR)
        val month = getEnum(Fields.MONTH).let(Month::valueOf)
        YearMonth(year = year, month = month)
    }

    private object Fields {
        const val MONTH = "month"
        const val YEAR = "year"
    }
}