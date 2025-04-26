package sollecitom.libs.pillar.json.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization.jsonSerde
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AuthorizationPrincipalJsonSerializationTests : AcmeJsonSerdeTestSpecification<AuthorizationPrincipal>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AuthorizationPrincipal.jsonSerde

    override fun parameterizedArguments() = listOf(
        "default-roles" to AuthorizationPrincipal.create()
    )
}