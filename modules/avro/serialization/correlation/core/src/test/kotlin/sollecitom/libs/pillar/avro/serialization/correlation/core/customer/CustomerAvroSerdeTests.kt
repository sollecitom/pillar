package sollecitom.libs.pillar.avro.serialization.correlation.core.customer

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class CustomerAvroSerdeTests : AcmeAvroSerdeTestSpecification<Customer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Customer.avroSerde

    override fun parameterizedArguments() = listOf(
        "isTest=true" to Customer.create(isTest = true),
        "isTest=false" to Customer.create(isTest = false)
    )
}