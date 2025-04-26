package sollecitom.libs.pillar.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.each
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.messaging.test.utils.event.processing.processAndWaitUntilAllAcked
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.testWithInvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.ddd.test.utils.create
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.test.utils.message.*

@TestInstance(PER_CLASS)
class MessagingEventProcessorTests : CoreDataGenerator by CoreDataGenerator.Companion.testProvider {

    @Test
    fun `processing events by reacting to them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asMessage(), testEvent2().asMessage())

        messages.processAndWaitUntilAllAcked { EventProcessingResult.Success }

        assertThat(messages).each {
            it.wasAcknowledgedSuccessfully()
            it.wasNotAcknowledgedAsFailed()
        }
    }

    @Test
    fun `processing events by ignoring them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asMessage(), testEvent2().asMessage())

        messages.processAndWaitUntilAllAcked { EventProcessingResult.NoOp }

        assertThat(messages).each {
            it.wasAcknowledgedSuccessfully()
            it.wasNotAcknowledgedAsFailed()
        }
    }

    @Test
    fun `failing to process an event`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asMessage(), testEvent2().asMessage())

        messages.processAndWaitUntilAllAcked { EventProcessingResult.Failure(IllegalStateException("A temporary error occurred")) }

        assertThat(messages).each {
            it.wasAcknowledgedAsFailed()
            it.wasNotAcknowledgedSuccessfully()
        }
    }

    private fun <EVENT : Event> EVENT.asMessage() = ReceivedMessage.Companion.inMemorySpy(this)

    private fun testEvent1(id: Id = newId(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEvent1(id, timestamp, context)

    private fun testEvent2(id: Id = newId(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEvent2(id, timestamp, context)

    private class TestEvent1(override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

        override val type: Happening.Type get() = TYPE

        companion object {
            val TYPE: Happening.Type = Happening.Type(name = "test-event-1".let(::Name), version = 1.let(::IntVersion))
        }
    }

    private class TestEvent2(override val id: Id, override val timestamp: Instant, override val context: Event.Context) : Event {

        override val type: Happening.Type get() = TYPE

        companion object {
            val TYPE: Happening.Type = Happening.Type(name = "test-event-2".let(::Name), version = 1.let(::IntVersion))
        }
    }
}