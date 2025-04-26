package sollecitom.libs.pillar.json.serialization.ddd.event

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.ddd.domain.Event
import kotlinx.datetime.Instant
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
        )
    )
}