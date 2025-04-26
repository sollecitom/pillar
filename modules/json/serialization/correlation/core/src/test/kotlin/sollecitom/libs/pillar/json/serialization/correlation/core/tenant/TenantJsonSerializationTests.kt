package sollecitom.libs.pillar.json.serialization.correlation.core.tenant

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TenantJsonSerializationTests : AcmeJsonSerdeTestSpecification<Tenant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Tenant.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-tenant" to Tenant.create()
    )
}