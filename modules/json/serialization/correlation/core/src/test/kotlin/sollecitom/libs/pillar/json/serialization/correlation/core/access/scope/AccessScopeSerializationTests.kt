package sollecitom.libs.pillar.json.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.scope.withContainerStack
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AccessScopeJsonSerializationTests : AcmeJsonSerdeTestSpecification<AccessScope>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccessScope.jsonSerde

    override fun parameterizedArguments() = listOf(
        "empty" to AccessScope.withContainerStack(),
        "single-container" to AccessScope.withContainerStack(AccessContainer.create()),
        "multiple-containers" to AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create())
    )
}