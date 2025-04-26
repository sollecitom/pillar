package sollecitom.libs.pillar.avro.serialization.correlation.core

import sollecitom.libs.pillar.avro.serialization.core.locale.localeAvroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.toggles.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.trace.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import org.apache.avro.generic.GenericRecord

val InvocationContext.Companion.avroSchema get() = InvocationContextAvroSchemas.invocationContext
val InvocationContext.Companion.avroSerde: AvroSerde<InvocationContext<*>> get() = InvocationContextAvroSerde

private object InvocationContextAvroSerde : AvroSerde<InvocationContext<*>> {

    override val schema get() = InvocationContext.avroSchema

    override fun serialize(value: InvocationContext<*>): GenericRecord = buildRecord {

        setValue(Fields.access, value.access, Access.avroSerde)
        setValue(Fields.trace, value.trace, Trace.avroSerde)
        setValue(Fields.toggles, value.toggles, Toggles.avroSerde)
        setValueOrNull(Fields.specifiedLocale, value.specifiedLocale, localeAvroSerde)
        setValueOrNull(Fields.specifiedTargetCustomer, value.specifiedTargetCustomer, Customer.avroSerde)
        setValueOrNull(Fields.specifiedTargetTenant, value.specifiedTargetTenant, Tenant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val access = getValue(Fields.access, Access.avroSerde)
        val trace = getValue(Fields.trace, Trace.avroSerde)
        val toggles = getValue(Fields.toggles, Toggles.avroSerde)
        val specifiedLocale = getValueOrNull(Fields.specifiedLocale, localeAvroSerde)
        val specifiedTargetCustomer = getValueOrNull(Fields.specifiedTargetCustomer, Customer.avroSerde)
        val specifiedTargetTenant = getValueOrNull(Fields.specifiedTargetTenant, Tenant.avroSerde)
        InvocationContext(access = access, trace = trace, toggles = toggles, specifiedLocale = specifiedLocale, specifiedTargetCustomer = specifiedTargetCustomer, specifiedTargetTenant = specifiedTargetTenant)
    }

    private object Fields {
        const val access = "access"
        const val trace = "trace"
        const val toggles = "toggles"
        const val specifiedLocale = "specified_locale"
        const val specifiedTargetCustomer = "specified_target_customer"
        const val specifiedTargetTenant = "specified_target_tenant"
    }
}