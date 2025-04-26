package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.customer.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.tenant.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject
import java.util.*

internal object UserAccountJsonSerde : JsonSerde.SchemaAware<Actor.UserAccount> {

    const val TYPE_VALUE = "user"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/UserAccount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.UserAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.LOCALE, value.locale?.toLanguageTag())
        setValue(Fields.CUSTOMER, value.customer, Customer.jsonSerde)
        setValue(Fields.TENANT, value.tenant, Tenant.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        val locale = getStringOrNull(Fields.LOCALE)?.let(Locale::forLanguageTag)
        val customer = getValue(Fields.CUSTOMER, Customer.jsonSerde)
        val tenant = getValue(Fields.TENANT, Tenant.jsonSerde)
        Actor.UserAccount(id = id, locale = locale, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val LOCALE = "locale"
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
    }
}

val Actor.UserAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.UserAccount> get() = UserAccountJsonSerde