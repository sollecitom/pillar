package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.correlation.core.test.utils.access.idp.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class FederatedSessionAvroSerdeTests : AcmeAvroSerdeTestSpecification<FederatedSession>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = FederatedSession.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomised" to FederatedSession(id = newId.external(), identityProvider = IdentityProvider.create())
    )
}