package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum
import sollecitom.libs.swissknife.avro.serialization.utils.getInt
import sollecitom.libs.swissknife.core.domain.time.MonthAndYear
import kotlinx.datetime.Month
import org.apache.avro.generic.GenericRecord
import java.time.Year

val MonthAndYear.Companion.avroSchema get() = TimeAvroSchemas.monthAndYear
val MonthAndYear.Companion.avroSerde: AvroSerde<MonthAndYear> get() = MonthAndYearAvroSerde

private object MonthAndYearAvroSerde : AvroSerde<MonthAndYear> {

    override val schema get() = MonthAndYear.avroSchema

    override fun serialize(value: MonthAndYear): GenericRecord = buildRecord {

        setEnum(Fields.MONTH, value.month.name)
        set(Fields.YEAR, value.year.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val month = getEnum(Fields.MONTH).let(Month::valueOf)
        val year = getInt(Fields.YEAR).let(Year::of)
        MonthAndYear(month = month, year = year)
    }

    private object Fields {
        const val MONTH = "month"
        const val YEAR = "year"
    }
}