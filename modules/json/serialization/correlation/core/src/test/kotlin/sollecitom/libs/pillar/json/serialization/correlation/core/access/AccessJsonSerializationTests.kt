package sollecitom.libs.pillar.json.serialization.correlation.core.access

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.unauthenticated
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AccessJsonSerializationTests : AcmeJsonSerdeTestSpecification<Access>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Access.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to Access.authenticated(),
        "normal-unauthenticated" to Access.unauthenticated(isTest = true),
        "test-unauthenticated" to Access.unauthenticated(isTest = false)
    )
}