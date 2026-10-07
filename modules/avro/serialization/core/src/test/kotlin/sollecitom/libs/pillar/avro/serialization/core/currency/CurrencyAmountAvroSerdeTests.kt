package sollecitom.libs.pillar.avro.serialization.core.currency

import assertk.assertThat
import assertk.assertions.hasMessage
import org.apache.avro.Conversions
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
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
class CurrencyAmountAvroSerdeTests : AcmeAvroSerdeTestSpecification<CurrencyAmount>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = CurrencyAmount.avroSerde

    override fun parameterizedArguments() = listOf(
        "dollars" to 23.8.dollars,
        "pounds" to 0.99.pounds,
        "euros" to 13.01.euros,
        "yens" to 185_203.yen,
        "generic" to GenericCurrencyAmount(units = 99.toBigInteger(), currency = Currency.GBP),
        "beyond-64-bits" to GenericCurrencyAmount(units = "123456789012345678901234567890".toBigInteger(), currency = Currency.USD)
    )

    @Test
    fun `negative units on the wire are rejected`() {

        val record = avroSerde.serialize(0.99.pounds).apply { put("units", Conversions.DecimalConversion().toBytes((-1).toBigDecimal(), schema.getField("units").schema(), schema.getField("units").schema().logicalType)) }
        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }

    @Test
    fun `an unknown currency fails with a clear error`() {

        val record = avroSerde.serializeWithUnknownEnumSymbol(0.99.pounds, fieldName = "currency")
        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().hasMessage("Field 'currency' is 'UNKNOWN': the writer used a symbol this reader doesn't know")
    }
}
