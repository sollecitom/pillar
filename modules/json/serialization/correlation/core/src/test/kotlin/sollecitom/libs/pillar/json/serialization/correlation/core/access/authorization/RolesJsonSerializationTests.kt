package sollecitom.libs.pillar.json.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization.jsonSerde
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authorization.TestRoles
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RolesJsonSerializationTests : AcmeJsonSerdeTestSpecification<Roles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Roles.jsonSerde

    override fun parameterizedArguments() = listOf(
        "test-roles" to TestRoles.default
    )
}