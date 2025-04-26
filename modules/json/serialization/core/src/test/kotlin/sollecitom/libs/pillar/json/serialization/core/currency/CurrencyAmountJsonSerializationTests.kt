package sollecitom.libs.pillar.json.serialization.core.currency

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.file.FileContent
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.core.file.jsonSerde
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.dollars
import sollecitom.libs.swissknife.core.domain.currency.known.euros
import sollecitom.libs.swissknife.core.domain.currency.known.pounds
import sollecitom.libs.swissknife.core.domain.currency.known.yen
import java.net.URI

@TestInstance(PER_CLASS)
class CurrencyAmountJsonSerializationTests : AcmeJsonSerdeTestSpecification<CurrencyAmount>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = CurrencyAmount.jsonSerde

    override fun parameterizedArguments() = listOf(
        "dollars" to 23.18.dollars,
        "pounds" to 0.pounds,
        "euros" to 0.99.euros,
        "yen" to 126_932.yen
    )
}