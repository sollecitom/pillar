package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.ServiceAccount
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.generic.GenericRecord

val ServiceAccount.External.Companion.avroSchema get() = ActorAvroSchemas.externalServiceAccount
val ServiceAccount.External.Companion.avroSerde: AvroSerde<ServiceAccount.External> get() = ExternalServiceAccountAvroSerde

private object ExternalServiceAccountAvroSerde : AvroSerde<ServiceAccount.External> {

    override val schema get() = ServiceAccount.External.avroSchema

    override fun serialize(value: ServiceAccount.External): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValue(Fields.customer, value.customer, Customer.avroSerde)
        setValue(Fields.tenant, value.tenant, Tenant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val customer = getValue(Fields.customer, Customer.avroSerde)
        val tenant = getValue(Fields.tenant, Tenant.avroSerde)
        ServiceAccount.External(id = id, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val id = "id"
        const val customer = "customer"
        const val tenant = "tenant"
    }
}