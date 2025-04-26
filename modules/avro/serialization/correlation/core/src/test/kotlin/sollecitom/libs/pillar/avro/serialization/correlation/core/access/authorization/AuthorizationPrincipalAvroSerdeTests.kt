package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AuthorizationPrincipalAvroSerdeTests : AcmeAvroSerdeTestSpecification<AuthorizationPrincipal>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = AuthorizationPrincipal.avroSerde

    override fun parameterizedArguments() = listOf(
        "random" to AuthorizationPrincipal.create()
    )
}