package sollecitom.libs.pillar.json.serialization.core.identity

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke

@TestInstance(PER_CLASS)
class IdJsonSerializationTests : AcmeJsonSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Id.jsonSerde

    override fun parameterizedArguments() = listOf(
        "internal" to newId(),
        "external" to newId.external(),
        "ULID" to newId.ulid.monotonic(),
        "KSUID" to newId.ksuid.monotonic(),
        "UUID" to newId.uuid.random(),
        "STRING" to StringId("whatever"),
    )
}