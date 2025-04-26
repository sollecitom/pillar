package sollecitom.libs.pillar.avro.serialization.core.currency

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.EUR
import sollecitom.libs.swissknife.core.domain.currency.known.GBP
import sollecitom.libs.swissknife.core.domain.currency.known.JPY
import sollecitom.libs.swissknife.core.domain.currency.known.USD
import sollecitom.libs.swissknife.core.domain.text.Name

val CurrencyAmount.Companion.avroSchema get() = CurrencyAvroSchemas.currencyAmount
val CurrencyAmount.Companion.avroSerde: AvroSerde<CurrencyAmount> get() = CurrencyAmountAvroSerde

private object CurrencyAmountAvroSerde : AvroSerde<CurrencyAmount> {

    override val schema get() = CurrencyAmount.avroSchema

    override fun serialize(value: CurrencyAmount): GenericRecord = buildRecord {

        setEnum(Fields.CURRENCY, value.currency.textualCode.value)
        set(Fields.UNITS, value.units.toString())
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val units = getString(Fields.UNITS).toBigInteger()
        val currency = when (val currencyCode = getEnum(Fields.CURRENCY).let(::Name)) {
            Currency.GBP.textualCode -> Currency.GBP
            Currency.USD.textualCode -> Currency.USD
            Currency.EUR.textualCode -> Currency.EUR
            Currency.JPY.textualCode -> Currency.JPY
            else -> error("Unsupported currency code '${currencyCode}'")
        }
        GenericCurrencyAmount(units = units, currency = currency)
    }

    private object Fields {
        const val CURRENCY = "currency"
        const val UNITS = "units"
    }
}