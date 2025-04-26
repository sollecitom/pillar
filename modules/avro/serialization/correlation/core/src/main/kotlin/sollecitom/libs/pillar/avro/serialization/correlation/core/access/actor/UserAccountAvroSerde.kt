package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.locale.localeAvroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSerde
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.UserAccount
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.generic.GenericRecord

val UserAccount.Companion.avroSchema get() = ActorAvroSchemas.userAccount
val UserAccount.Companion.avroSerde: AvroSerde<UserAccount> get() = UserAccountAvroSerde

private object UserAccountAvroSerde : AvroSerde<UserAccount> {

    override val schema get() = UserAccount.avroSchema

    override fun serialize(value: UserAccount): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValueOrNull(Fields.locale, value.locale, localeAvroSerde)
        setValue(Fields.customer, value.customer, Customer.avroSerde)
        setValue(Fields.tenant, value.tenant, Tenant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val locale = getValueOrNull(Fields.locale, localeAvroSerde)
        val customer = getValue(Fields.customer, Customer.avroSerde)
        val tenant = getValue(Fields.tenant, Tenant.avroSerde)
        UserAccount(id = id, locale = locale, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val id = "id"
        const val locale = "locale"
        const val customer = "customer"
        const val tenant = "tenant"
    }
}