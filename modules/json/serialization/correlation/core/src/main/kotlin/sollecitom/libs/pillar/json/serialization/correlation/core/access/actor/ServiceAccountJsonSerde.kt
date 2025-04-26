package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.customer.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.tenant.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.*
import org.json.JSONObject

internal object ServiceAccountJsonSerde : JsonSerde.SchemaAware<Actor.ServiceAccount> {

    const val TYPE_VALUE = "service"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/ServiceAccount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.ServiceAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        setValueOrNull(Fields.CUSTOMER, value.customer, Customer.jsonSerde)
        if (value is Actor.ServiceAccount.External) {
            setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
        }
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        when (val customer = getValueOrNull(Fields.CUSTOMER, Customer.jsonSerde)) {
            null -> Actor.ServiceAccount.Internal(id = id)
            else -> {
                val tenant = getValue(Fields.TENANT, Tenant.jsonSerde)
                Actor.ServiceAccount.External(id = id, customer = customer, tenant = tenant)
            }
        }
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
    }
}

val Actor.ServiceAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.ServiceAccount> get() = ServiceAccountJsonSerde