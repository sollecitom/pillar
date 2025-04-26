package sollecitom.libs.pillar.json.serialization.correlation.core.customer

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object CustomerJsonSerde : JsonSerde.SchemaAware<Customer> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/customer/Customer.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Customer) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.IS_TEST, value.isTest)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        val isTest = getBoolean(Fields.IS_TEST)
        Customer(id = id, isTest = isTest)
    }

    private object Fields {
        const val ID = "id"
        const val IS_TEST = "is-test"
    }
}

val Customer.Companion.jsonSerde: JsonSerde.SchemaAware<Customer> get() = CustomerJsonSerde