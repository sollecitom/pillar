package sollecitom.libs.pillar.avro.serialization.core.currency

import org.apache.avro.Conversions
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.pillar.avro.serialization.core.getKnownEnum
import java.nio.ByteBuffer
import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.EUR
import sollecitom.libs.swissknife.core.domain.currency.known.GBP
import sollecitom.libs.swissknife.core.domain.currency.known.JPY
import sollecitom.libs.swissknife.core.domain.currency.known.USD
import sollecitom.libs.swissknife.core.domain.text.Name

/** Avro schema for [CurrencyAmount]. */
val CurrencyAmount.Companion.avroSchema get() = CurrencyAvroSchemas.currencyAmount
/** Avro serializer/deserializer for [CurrencyAmount]. Supports GBP, USD, EUR, and JPY. Units are a non-negative big integer, encoded as a bytes decimal. */
val CurrencyAmount.Companion.avroSerde: AvroSerde<CurrencyAmount> get() = CurrencyAmountAvroSerde

private object CurrencyAmountAvroSerde : AvroSerde<CurrencyAmount> {

    override val schema get() = CurrencyAmount.avroSchema

    private val unitsSchema get() = schema.getField(Fields.UNITS).schema()
    private val currencySchema get() = schema.getField(Fields.CURRENCY).schema()
    private val decimalConversion = Conversions.DecimalConversion()

    override fun serialize(value: CurrencyAmount): GenericRecord = GenericData.Record(schema).apply {

        put(Fields.UNITS, decimalConversion.toBytes(value.units.toBigDecimal(), unitsSchema, unitsSchema.logicalType))
        put(Fields.CURRENCY, GenericData.EnumSymbol(currencySchema, value.currency.textualCode.value))
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val units = decimalConversion.fromBytes((get(Fields.UNITS) as ByteBuffer).duplicate(), unitsSchema, unitsSchema.logicalType).toBigIntegerExact()
        val currency = when (val currencyCode = getKnownEnum(Fields.CURRENCY).let(::Name)) {
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