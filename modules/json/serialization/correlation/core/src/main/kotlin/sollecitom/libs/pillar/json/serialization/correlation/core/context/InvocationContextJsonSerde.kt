package sollecitom.libs.pillar.json.serialization.correlation.core.context

import sollecitom.libs.pillar.json.serialization.correlation.core.access.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.customer.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.tenant.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.toggles.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.trace.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.*
import org.json.JSONObject
import java.util.*

private object InvocationContextJsonSerde : JsonSerde.SchemaAware<InvocationContext<*>> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/context/InvocationContext.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: InvocationContext<*>) = JSONObject().apply {
        setValue(Fields.ACCESS, value.access, Access.jsonSerde)
        setValue(Fields.TRACE, value.trace, Trace.jsonSerde)
        setValue(Fields.TOGGLES, value.toggles, Toggles.jsonSerde)
        put(Fields.SPECIFIED_LOCALE, value.specifiedLocale?.toLanguageTag())
        setValueOrNull(Fields.SPECIFIED_TARGET_CUSTOMER, value.specifiedTargetCustomer, Customer.jsonSerde)
        setValueOrNull(Fields.SPECIFIED_TARGET_TENANT, value.specifiedTargetTenant, Tenant.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val access = getValue(Fields.ACCESS, Access.jsonSerde)
        val trace = getValue(Fields.TRACE, Trace.jsonSerde)
        val toggles = getValue(Fields.TOGGLES, Toggles.jsonSerde)
        val specifiedLocale = getStringOrNull(Fields.SPECIFIED_LOCALE)?.let(Locale::forLanguageTag)
        val specifiedTargetCustomer = getValueOrNull(Fields.SPECIFIED_TARGET_CUSTOMER, Customer.jsonSerde)
        val specifiedTargetTenant = getValueOrNull(Fields.SPECIFIED_TARGET_TENANT, Tenant.jsonSerde)
        InvocationContext(access = access, trace = trace, toggles = toggles, specifiedLocale = specifiedLocale, specifiedTargetCustomer = specifiedTargetCustomer, specifiedTargetTenant = specifiedTargetTenant)
    }

    private object Fields {
        const val ACCESS = "access"
        const val TRACE = "trace"
        const val TOGGLES = "toggles"
        const val SPECIFIED_TARGET_CUSTOMER = "specified-target-customer"
        const val SPECIFIED_TARGET_TENANT = "specified-target-tenant"
        const val SPECIFIED_LOCALE = "specified-locale"
    }
}

val InvocationContext.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationContext<*>> get() = InvocationContextJsonSerde