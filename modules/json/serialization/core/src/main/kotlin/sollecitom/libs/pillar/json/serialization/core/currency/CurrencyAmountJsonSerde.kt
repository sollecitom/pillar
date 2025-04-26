package sollecitom.libs.pillar.json.serialization.core.currency

import org.json.JSONObject
import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.known.EUR
import sollecitom.libs.swissknife.core.domain.currency.known.GBP
import sollecitom.libs.swissknife.core.domain.currency.known.JPY
import sollecitom.libs.swissknife.core.domain.currency.known.USD
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.json.utils.getRequiredBigInteger
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde

val CurrencyAmount.Companion.jsonSerde: JsonSerde.SchemaAware<CurrencyAmount> get() = CurrencyAmountJsonSerde

internal object CurrencyAmountJsonSerde : JsonSerde.SchemaAware<CurrencyAmount> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/currency/CurrencyAmount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: CurrencyAmount) = JSONObject().apply {
        put(Fields.UNITS, value.units)
        put(Fields.CURRENCY, value.currency.textualCode.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val units = getRequiredBigInteger(Fields.UNITS)
        val currency = when (val currencyCode = getRequiredString(Fields.CURRENCY).let(::Name)) {
            Currency.GBP.textualCode -> Currency.GBP
            Currency.USD.textualCode -> Currency.USD
            Currency.EUR.textualCode -> Currency.EUR
            Currency.JPY.textualCode -> Currency.JPY
            else -> error("Unsupported currency code '${currencyCode}'")
        }
        GenericCurrencyAmount(units = units, currency = currency)
    }

    private object Fields {
        const val UNITS = "units"
        const val CURRENCY = "currency"
    }
}