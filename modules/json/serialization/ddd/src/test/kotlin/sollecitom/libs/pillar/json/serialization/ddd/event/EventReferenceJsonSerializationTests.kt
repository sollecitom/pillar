package sollecitom.libs.pillar.json.serialization.ddd.event

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class EventReferenceJsonSerializationTests : AcmeJsonSerdeTestSpecification<Event.Reference>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Event.Reference.jsonSerde

    override fun parameterizedArguments() = listOf(
        "stubbed" to Event.Reference(id = newId.external(), type = Happening.Type(name = Name("account-created"), version = IntVersion(value = 2)), timestamp = Instant.fromEpochMilliseconds(1700000000000))
    )
}
