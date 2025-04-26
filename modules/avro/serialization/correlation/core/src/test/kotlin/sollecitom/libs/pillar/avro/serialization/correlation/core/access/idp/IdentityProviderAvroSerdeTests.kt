package sollecitom.libs.pillar.avro.serialization.correlation.core.access.idp

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.test.utils.access.idp.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class IdentityProviderAvroSerdeTests : AcmeAvroSerdeTestSpecification<IdentityProvider>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = IdentityProvider.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomised" to IdentityProvider.create()
    )
}