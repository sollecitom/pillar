package sollecitom.libs.pillar.avro.serialization.correlation.core.customer

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import org.apache.avro.generic.GenericRecord

val Customer.Companion.avroSchema get() = CustomerAvroSchemas.customer
val Customer.Companion.avroSerde: AvroSerde<Customer> get() = CustomerAvroSerde

private object CustomerAvroSerde : AvroSerde<Customer> {

    override val schema get() = Customer.avroSchema

    override fun serialize(value: Customer): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        set(Fields.isTest, value.isTest)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val isTest = getBoolean(Fields.isTest)
        Customer(id = id, isTest = isTest)
    }

    private object Fields {
        const val id = "id"
        const val isTest = "is_test"
    }
}