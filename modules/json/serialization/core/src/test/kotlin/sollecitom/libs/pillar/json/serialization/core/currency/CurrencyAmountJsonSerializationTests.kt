package sollecitom.libs.pillar.json.serialization.core.currency

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import assertk.assertThat
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.USD
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.dollars
import sollecitom.libs.swissknife.core.domain.currency.known.euros
import sollecitom.libs.swissknife.core.domain.currency.known.pounds
import sollecitom.libs.swissknife.core.domain.currency.known.yen

@TestInstance(PER_CLASS)
class CurrencyAmountJsonSerializationTests : AcmeJsonSerdeTestSpecification<CurrencyAmount>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = CurrencyAmount.jsonSerde

    override fun parameterizedArguments() = listOf(
        "dollars" to 23.18.dollars,
        "pounds" to 0.pounds,
        "euros" to 0.99.euros,
        "yen" to 126_932.yen,
        "beyond-js-safe-integers" to GenericCurrencyAmount(units = "123456789012345678901234567890".toBigInteger(), currency = Currency.USD)
    )

    @ParameterizedTest
    @ValueSource(strings = ["-1", "+1", "1.5", ""])
    fun `units that aren't a non-negative integer are rejected`(units: String) {

        val json = jsonSerde.serialize(0.99.pounds).put("units", units)
        val result = runCatching { jsonSerde.deserialize(json) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }
}