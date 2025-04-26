package sollecitom.libs.pillar.json.serialization.correlation.core.customer

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CustomerJsonSerializationTests : AcmeJsonSerdeTestSpecification<Customer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Customer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "normal-customer" to Customer.create(isTest = false),
        "test-customer" to Customer.create(isTest = true)
    )
}