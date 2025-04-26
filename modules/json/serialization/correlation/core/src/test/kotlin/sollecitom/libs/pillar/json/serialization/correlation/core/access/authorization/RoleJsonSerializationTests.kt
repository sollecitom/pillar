package sollecitom.libs.pillar.json.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization.jsonSerde
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RoleJsonSerializationTests : AcmeJsonSerdeTestSpecification<Role>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Role.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-role" to Role("some-role".let(::Name))
    )
}