package sollecitom.libs.pillar.json.serialization.core.time

import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification

@TestInstance(PER_CLASS)
class MonthAndYearJsonSerializationTests : AcmeJsonSerdeTestSpecification<YearMonth> {

    override val jsonSerde get() = YearMonth.jsonSerde

    override fun parameterizedArguments() = listOf(
        "january" to YearMonth(year = 2024, month = Month.JANUARY),
        "december" to YearMonth(year = 1999, month = Month.DECEMBER),
        "far-future" to YearMonth(year = 2150, month = Month.JULY)
    )
}
