package sollecitom.libs.pillar.avro.serialization.core.currency

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.pillar.avro.serialization.test.utils.serializeWithUnknownEnumSymbol
import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.*
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
class CurrencyAmountAvroSerdeTests : AcmeAvroSerdeTestSpecification<CurrencyAmount>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = CurrencyAmount.avroSerde

    override fun parameterizedArguments() = listOf(
        "dollars" to 23.8.dollars,
        "pounds" to 0.99.pounds,
        "euros" to 13.01.euros,
        "yens" to 185_203.yen,
        "generic" to GenericCurrencyAmount(units = 99.toBigInteger(), currency = Currency.GBP)
    )

    @Test
    fun `an unknown currency fails with a clear error`() {

        val record = avroSerde.serializeWithUnknownEnumSymbol(0.99.pounds, fieldName = "currency")

        assertFailure { avroSerde.deserialize(record) }.isInstanceOf<IllegalStateException>().hasMessage("Field 'currency' is 'UNKNOWN': the writer used a symbol this reader doesn't know")
    }
}
