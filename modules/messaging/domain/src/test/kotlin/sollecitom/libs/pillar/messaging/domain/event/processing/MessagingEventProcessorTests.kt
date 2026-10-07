package sollecitom.libs.pillar.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.each
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import assertk.assertions.prop
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.messaging.test.utils.event.processing.processAndWaitUntilAllAcked
import sollecitom.libs.pillar.messaging.test.utils.message.UndecodableMessageSpy
import sollecitom.libs.pillar.messaging.test.utils.message.asReceivedEventSpy
import sollecitom.libs.pillar.messaging.test.utils.message.withType
import sollecitom.libs.pillar.messaging.test.utils.message.withoutType
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.testWithInvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.ddd.test.utils.create
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult
import sollecitom.libs.swissknife.messaging.domain.event.processing.ProcessEvent
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.test.utils.message.*
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class MessagingEventProcessorTests : CoreDataGenerator by CoreDataGenerator.Companion.testProvider {

    @Test
    fun `processing events by reacting to them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asReceivedEventSpy(), testEvent2().asReceivedEventSpy())

        messages.processAndWaitUntilAllAcked(handlingBothTypes { EventProcessingResult.Success })

        assertThat(messages).each { it.wasAcknowledgedSuccessfully() }
    }

    @Test
    fun `processing events by ignoring them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asReceivedEventSpy(), testEvent2().asReceivedEventSpy())

        messages.processAndWaitUntilAllAcked(handlingBothTypes { EventProcessingResult.NoOp })

        assertThat(messages).each { it.wasAcknowledgedSuccessfully() }
    }

    @Test
    fun `events are dispatched to the handler registered for their type`() = testWithInvocationContext {

        val event1 = testEvent1()
        val event2 = testEvent2()
        val messages = listOf<ReceivedMessageSpy<Event>>(event1.asReceivedEventSpy(), event2.asReceivedEventSpy())
        val processedByHandler1 = mutableListOf<Event>()
        val processedByHandler2 = mutableListOf<Event>()
        val handler = EventHandler.byType<Event>(mapOf(
            TestEvent1.TYPE to ProcessEvent { message -> processedByHandler1 += message.value; EventProcessingResult.Success },
            TestEvent2.TYPE to ProcessEvent { message -> processedByHandler2 += message.value; EventProcessingResult.Success }
        ))

        messages.processAndWaitUntilAllAcked(handler)

        assertThat(processedByHandler1).containsExactly(event1)
        assertThat(processedByHandler2).containsExactly(event2)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `a message that fails to process is retried in place until it succeeds, before the next one is processed`() = runTest {

        val failing = testEvent1()
        val following = testEvent2()
        val messages = listOf<ReceivedMessageSpy<Event>>(failing.asReceivedEventSpy(), following.asReceivedEventSpy())
        val attempts = mutableListOf<Event>()
        val processor = EventProcessor.withMessages(messages.asFlow(), handlingBothTypes { message ->
            attempts += message.value
            if (message.value == failing && attempts.size <= 2) error("A temporary error occurred")
            EventProcessingResult.Success
        }, scope = backgroundScope)

        processor.start()
        messages.waitUntilAllAcked()

        assertThat(attempts).containsExactly(failing, failing, failing, following)
        assertThat(testScheduler.currentTime).isEqualTo(3.seconds.inWholeMilliseconds)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `the delay between retries doubles up to a minute`() = runTest {

        val message = testEvent1().asReceivedEventSpy()
        var attempts = 0
        val processor = EventProcessor.withMessages(flowOf(message), handlingBothTypes {
            if (++attempts <= 8) error("A temporary error occurred")
            EventProcessingResult.Success
        }, scope = backgroundScope)

        processor.start()
        message.awaitSuccessfulAck()

        assertThat(testScheduler.currentTime).isEqualTo((1 + 2 + 4 + 8 + 16 + 32 + 60 + 60).seconds.inWholeMilliseconds)
    }

    @Test
    fun `stopping the processor stops retrying a failing message`() = runTest {

        val message = testEvent1().asReceivedEventSpy()
        var attempts = 0
        val processor = EventProcessor.withMessages(flowOf(message), handlingBothTypes {
            attempts++
            error("A bug")
        }, scope = backgroundScope)
        processor.start()
        delay(10.seconds)
        val attemptsBeforeStopping = attempts

        processor.stop()
        delay(10.minutes)

        assertThat(attempts).isEqualTo(attemptsBeforeStopping)
        assertThat(message.wasAcknowledgedSuccessfully).isFalse()
    }

    @Test
    fun `a message of an unhandled type is acknowledged without decoding it`() = runTest {

        val undecodable = UndecodableMessageSpy.withType<Event>(TestEvent2.TYPE)
        val handled = testEvent1().asReceivedEventSpy()
        val processed = mutableListOf<Event>()
        val processor = EventProcessor.withMessages(flowOf(undecodable, handled), handling(TestEvent1.TYPE) { message ->
            processed += message.value
            EventProcessingResult.Success
        }, scope = backgroundScope)

        processor.start()
        handled.awaitSuccessfulAck()

        assertThat(undecodable.wasAcknowledged).isTrue()
        assertThat(processed).containsExactly(handled.value)
    }

    @Test
    fun `an undecodable message of a handled type halts the processor without acknowledging it`() = runTest {

        val undecodable = UndecodableMessageSpy.withType<Event>(TestEvent1.TYPE)
        val following = testEvent1().asReceivedEventSpy()
        val processed = mutableListOf<Event>()
        val halted = CompletableDeferred<UndecodableMessageException>()
        val processor = EventProcessor.withMessages(flowOf(undecodable, following), handling(TestEvent1.TYPE) { message ->
            processed += message.value
            EventProcessingResult.Success
        }, scope = backgroundScope, onUndecodableMessage = halted::complete)

        processor.start()
        val failure = halted.await()

        assertThat(failure).prop(UndecodableMessageException::messageId).isEqualTo(undecodable.id)
        assertThat(failure).prop(UndecodableMessageException::eventType).isEqualTo(TestEvent1.TYPE.stringValue)
        assertThat(undecodable.wasAcknowledged).isFalse()
        assertThat(following.wasAcknowledgedSuccessfully).isFalse()
        assertThat(processed).isEmpty()
    }

    @Test
    fun `a message without an event type halts the processor without acknowledging it`() = runTest {

        val untyped = UndecodableMessageSpy.withoutType<Event>()
        val halted = CompletableDeferred<UndecodableMessageException>()
        val processor = EventProcessor.withMessages(flowOf(untyped), handling(TestEvent1.TYPE) { EventProcessingResult.Success }, scope = backgroundScope, onUndecodableMessage = halted::complete)

        processor.start()
        val failure = halted.await()

        assertThat(failure).prop(UndecodableMessageException::messageId).isEqualTo(untyped.id)
        assertThat(failure).prop(UndecodableMessageException::eventType).isNull()
        assertThat(untyped.wasAcknowledged).isFalse()
    }

    @Test
    fun `an unknown version of a handled event type is not acknowledged`() = runTest {

        val newerVersion = testEvent1(type = TestEvent1.TYPE.copy(version = 2.let(::IntVersion))).asReceivedEventSpy()
        val handler = EventHandler.byType<Event>(mapOf(TestEvent1.TYPE to ProcessEvent { EventProcessingResult.Success }))
        val processor = EventProcessor.withMessages(flowOf(newerVersion), handler, scope = backgroundScope)

        processor.start()
        delay(10.minutes)

        assertThat(newerVersion.wasAcknowledgedSuccessfully).isFalse()
    }

    @Test
    fun `a processor must handle at least one event type`() {

        val result = runCatching { EventProcessor.withMessages(flowOf(), handling { EventProcessingResult.Success }) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }

    private fun handlingBothTypes(process: suspend (ReceivedMessage<Event>) -> EventProcessingResult) = handling(TestEvent1.TYPE, TestEvent2.TYPE, process = process)

    private fun handling(vararg types: Happening.Type, process: suspend (ReceivedMessage<Event>) -> EventProcessingResult) = EventHandler.byType(types.associateWith { ProcessEvent<Event> { message -> process(message) } })

    private fun testEvent1(id: Id = newId(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create(), type: Happening.Type = TestEvent1.TYPE) = TestEvent1(id, timestamp, context, type)

    private fun testEvent2(id: Id = newId(), timestamp: Instant = clock.now(), context: Event.Context = Event.Context.create()) = TestEvent2(id, timestamp, context)

    private class TestEvent1(override val id: Id, override val timestamp: Instant, override val context: Event.Context, override val type: Happening.Type) : Event {

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
