package sollecitom.libs.pillar.json.serialization.ddd.event

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import kotlin.time.Instant
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EventMetadataJsonSerdeTest : AcmeJsonSerdeTestSpecification<Event.Metadata>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Event.Metadata.jsonSerde

    override fun parameterizedArguments() = listOf(
        "event-metadata" to Event.Metadata(
            id = newId.external(),
            timestamp = Instant.fromEpochMilliseconds(0),
            context = Event.Context(InvocationContext.authenticated())
        ),
        "event-metadata-with-parent-and-originating-references" to Event.Metadata(
            id = newId.external(),
            timestamp = Instant.fromEpochMilliseconds(0),
            context = Event.Context(
                invocation = InvocationContext.authenticated(),
                parent = Event.Reference(id = newId.external(), type = Happening.Type(name = Name("parent-event"), version = IntVersion(value = 1)), timestamp = Instant.fromEpochMilliseconds(1700000000000)),
                originating = Event.Reference(id = newId.external(), type = Happening.Type(name = Name("originating-event"), version = IntVersion(value = 3)), timestamp = Instant.fromEpochMilliseconds(1600000000000))
            )
        )
    )
}