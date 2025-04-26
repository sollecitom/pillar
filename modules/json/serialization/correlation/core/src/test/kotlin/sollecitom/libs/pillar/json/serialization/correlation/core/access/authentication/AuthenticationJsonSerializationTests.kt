package sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.credentialsBased
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.federated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authentication.stateless
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AuthenticationJsonSerializationTests : AcmeJsonSerdeTestSpecification<Authentication>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Authentication.jsonSerde

    override fun parameterizedArguments() = listOf(
        "credentials-based" to Authentication.credentialsBased(),
        "federated" to Authentication.federated(),
        "stateless" to Authentication.stateless()
    )
}