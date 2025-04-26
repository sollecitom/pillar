package sollecitom.libs.pillar.json.serialization.correlation.core.access.idp

import sollecitom.libs.pillar.json.serialization.correlation.core.customer.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.tenant.jsonSerde
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object IdentityProviderJsonSerde : JsonSerde.SchemaAware<IdentityProvider> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/idp/IdentityProvider.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: IdentityProvider) = JSONObject().apply {
        setValue(Fields.CUSTOMER, value.customer, Customer.jsonSerde)
        setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
        put(Fields.NAME, value.name.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val customer = getValue(Fields.CUSTOMER, Customer.jsonSerde)
        val tenant = getValue(Fields.TENANT, Tenant.jsonSerde)
        val name = getRequiredString(Fields.NAME).let(::Name)
        IdentityProvider(name = name, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
        const val NAME = "name"
    }
}

val IdentityProvider.Companion.jsonSerde: JsonSerde.SchemaAware<IdentityProvider> get() = IdentityProviderJsonSerde