package sollecitom.libs.pillar.avro.serialization.core.currency

import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.avro.generic.GenericData
import assertk.assertions.messageContains
import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
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
    fun `a currency this reader does not know fails with a clear error`() {

        val record = GenericRecordBuilder(avroSerde.schema).set("currency", unknownSymbolOf("currency")).set("units", "1").build()

        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().messageContains("the writer used a currency this reader doesn't know")
    }

    private fun unknownSymbolOf(fieldName: String) = GenericData.EnumSymbol(avroSerde.schema.getField(fieldName).schema(), "UNKNOWN")
}
