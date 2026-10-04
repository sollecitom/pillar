package sollecitom.libs.pillar.json.serialization.ddd.event

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.unauthenticated
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class EventContextJsonSerializationTests : AcmeJsonSerdeTestSpecification<Event.Context>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Event.Context.jsonSerde

    override fun parameterizedArguments() = listOf(
        "fully-populated" to Event.Context(invocation = InvocationContext.authenticated(), parent = reference("parent-event"), originating = reference("originating-event")),
        "with-null-parent-and-originating-references" to Event.Context(invocation = InvocationContext.unauthenticated(), parent = null, originating = null)
    )

    private fun reference(typeName: String) = Event.Reference(id = newId.external(), type = Happening.Type(name = Name(typeName), version = IntVersion(value = 2)), timestamp = Instant.fromEpochMilliseconds(1700000000000))
}
