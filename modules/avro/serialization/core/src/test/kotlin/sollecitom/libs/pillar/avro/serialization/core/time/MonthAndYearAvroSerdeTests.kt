package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.avro.generic.GenericData
import assertk.assertions.messageContains
import assertk.assertThat
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.time.monthAndYear
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.kotlin.extensions.time.localDate

@TestInstance(PER_CLASS)
class MonthAndYearAvroSerdeTests : AcmeAvroSerdeTestSpecification<YearMonth>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = YearMonth.avroSerde

    override fun parameterizedArguments() = listOf(
        "now" to clock.localDate().monthAndYear,
        "3 years ago" to clock.localDate().minus(3, DateTimeUnit.YEAR).monthAndYear,
        "in 11 years" to clock.localDate().plus(11, DateTimeUnit.YEAR).monthAndYear,
        "5 months ago" to clock.localDate().minus(5, DateTimeUnit.MONTH).monthAndYear,
        "in 41 months" to clock.localDate().plus(41, DateTimeUnit.MONTH).monthAndYear,
    )

    @Test
    fun `a month this reader does not know fails with a clear error`() {

        val record = GenericRecordBuilder(avroSerde.schema).set("month", unknownSymbolOf("month")).set("year", 2026).build()

        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().messageContains("the writer used a month this reader doesn't know")
    }

    private fun unknownSymbolOf(fieldName: String) = GenericData.EnumSymbol(avroSerde.schema.getField(fieldName).schema(), "UNKNOWN")
}
