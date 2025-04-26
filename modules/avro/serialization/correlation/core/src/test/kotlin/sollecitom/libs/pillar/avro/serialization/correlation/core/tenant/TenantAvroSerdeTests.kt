package sollecitom.libs.pillar.avro.serialization.correlation.core.tenant

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class TenantAvroSerdeTests : AcmeAvroSerdeTestSpecification<Tenant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Tenant.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomised" to Tenant.create()
    )
}