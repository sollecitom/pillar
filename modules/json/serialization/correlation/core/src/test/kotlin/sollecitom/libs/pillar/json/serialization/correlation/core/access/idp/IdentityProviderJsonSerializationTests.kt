package sollecitom.libs.pillar.json.serialization.correlation.core.access.idp

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class IdentityProviderJsonSerializationTests : AcmeJsonSerdeTestSpecification<IdentityProvider>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = IdentityProvider.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-IDP" to IdentityProvider(name = "Some IDP".let(::Name), customer = Customer.create(), tenant = Tenant.create())
    )
}