package sollecitom.libs.pillar.avro.serialization.core.time

import assertk.assertThat
import assertk.assertions.hasMessage
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.pillar.avro.serialization.test.utils.serializeWithUnknownEnumSymbol
import sollecitom.libs.swissknife.core.domain.time.monthAndYear
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.kotlin.extensions.time.localDate
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

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
    fun `an unknown month fails with a clear error`() {

        val record = avroSerde.serializeWithUnknownEnumSymbol(YearMonth(2026, Month.OCTOBER), fieldName = "month")
        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().hasMessage("Field 'month' is 'UNKNOWN': the writer used a symbol this reader doesn't know")
    }
}
