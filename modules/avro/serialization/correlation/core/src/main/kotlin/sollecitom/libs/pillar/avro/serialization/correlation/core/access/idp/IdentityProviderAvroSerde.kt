package sollecitom.libs.pillar.avro.serialization.correlation.core.access.idp

import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.generic.GenericRecord

val IdentityProvider.Companion.avroSchema get() = IdentityProviderAvroSchemas.identityProvider
val IdentityProvider.Companion.avroSerde: AvroSerde<IdentityProvider> get() = IdentityProviderAvroSerde

private object IdentityProviderAvroSerde : AvroSerde<IdentityProvider> {

    override val schema get() = IdentityProvider.avroSchema

    override fun serialize(value: IdentityProvider): GenericRecord = buildRecord {

        set(Fields.name, value.name.value)
        setValue(Fields.customer, value.customer, Customer.avroSerde)
        setValue(Fields.tenant, value.tenant, Tenant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val name = getString(Fields.name).let(::Name)
        val customer = getValue(Fields.customer, Customer.avroSerde)
        val tenant = getValue(Fields.tenant, Tenant.avroSerde)
        IdentityProvider(name = name, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val name = "name"
        const val customer = "customer"
        const val tenant = "tenant"
    }
}