package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.time.MonthAndYear
import sollecitom.libs.swissknife.core.domain.time.monthAndYear
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.kotlin.extensions.time.localDate
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class MonthAndYearAvroSerdeTests : AcmeAvroSerdeTestSpecification<MonthAndYear>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = MonthAndYear.avroSerde

    override fun parameterizedArguments() = listOf(
        "now" to clock.localDate().monthAndYear,
        "3 years ago" to clock.localDate().minus(3, DateTimeUnit.YEAR).monthAndYear,
        "in 11 years" to clock.localDate().plus(11, DateTimeUnit.YEAR).monthAndYear,
        "5 months ago" to clock.localDate().minus(5, DateTimeUnit.MONTH).monthAndYear,
        "in 41 months" to clock.localDate().plus(41, DateTimeUnit.MONTH).monthAndYear,
    )
}