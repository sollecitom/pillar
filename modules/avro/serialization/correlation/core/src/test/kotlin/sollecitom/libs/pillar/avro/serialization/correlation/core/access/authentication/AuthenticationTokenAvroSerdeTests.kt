package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AuthenticationTokenAvroSerdeTests : AcmeAvroSerdeTestSpecification<Authentication.Token>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Authentication.Token.avroSerde

    override fun parameterizedArguments() = listOf(
        "scoped" to Authentication.Token.create(),
        "validFrom=null" to Authentication.Token.create(validFrom = null),
        "validTo=null" to Authentication.Token.create(validTo = null),
    )
}