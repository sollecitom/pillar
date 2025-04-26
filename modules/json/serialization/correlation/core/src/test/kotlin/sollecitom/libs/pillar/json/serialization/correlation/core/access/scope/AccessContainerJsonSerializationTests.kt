package sollecitom.libs.pillar.json.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AccessContainerJsonSerializationTests : AcmeJsonSerdeTestSpecification<AccessContainer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccessContainer.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to AccessContainer.create()
    )
}