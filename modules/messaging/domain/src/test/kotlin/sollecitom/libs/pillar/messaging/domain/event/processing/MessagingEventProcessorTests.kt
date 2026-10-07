package sollecitom.libs.pillar.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
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
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.ddd.test.utils.create
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult
import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.messaging.test.utils.topic.create
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.messaging.test.utils.message.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
class MessagingEventProcessorTests : CoreDataGenerator by CoreDataGenerator.Companion.testProvider {

    @Test
    fun `processing events by reacting to them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asMessage(), testEvent2().asMessage())

        messages.processAndWaitUntilAllAcked { EventProcessingResult.Success }

        assertThat(messages).each { it.wasAcknowledgedSuccessfully() }
    }

    @Test
    fun `processing events by ignoring them at application level`() = testWithInvocationContext {

        val messages = listOf<ReceivedMessageSpy<Event>>(testEvent1().asMessage(), testEvent2().asMessage())

        messages.processAndWaitUntilAllAcked { EventProcessingResult.NoOp }

        assertThat(messages).each { it.wasAcknowledgedSuccessfully() }
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `a message that fails to process is retried in place until it succeeds, before the next one is processed`() = runTest {

        val failing = testEvent1()
        val following = testEvent2()
        val messages = listOf<ReceivedMessageSpy<Event>>(failing.asMessage(), following.asMessage())
        val attempts = mutableListOf<Event>()
        val processor = EventProcessor.withMessages(messages.asFlow(), handledTypes = handledTypes, scope = backgroundScope, processEvent = { message ->
            attempts += message.value
            if (message.value == failing && attempts.size <= 2) error("A temporary error occurred")
            EventProcessingResult.Success
        })

        processor.start()
        messages.waitUntilAllAcked()

        assertThat(attempts).containsExactly(failing, failing, failing, following)
        assertThat(testScheduler.currentTime).isEqualTo(3.seconds.inWholeMilliseconds)
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun `the delay between retries doubles up to a minute`() = runTest {

        val message = testEvent1().asMessage()
        var attempts = 0
        val processor = EventProcessor.withMessages(flowOf(message), handledTypes = handledTypes, scope = backgroundScope, processEvent = {
            if (++attempts <= 8) error("A temporary error occurred")
            EventProcessingResult.Success
        })

        processor.start()
        message.awaitSuccessfulAck()

        assertThat(testScheduler.currentTime).isEqualTo((1 + 2 + 4 + 8 + 16 + 32 + 60 + 60).seconds.inWholeMilliseconds)
    }

    @Test
    fun `stopping the processor stops retrying a failing message`() = runTest {

        val message = testEvent1().asMessage()
        var attempts = 0
        val processor = EventProcessor.withMessages(flowOf(message), handledTypes = handledTypes, scope = backgroundScope, processEvent = {
            attempts++
            error("A bug")
        })
        processor.start()
        delay(10.seconds)
        val attemptsBeforeStopping = attempts

        processor.stop()
        delay(10.minutes)

        assertThat(attempts).isEqualTo(attemptsBeforeStopping)
        assertThat(message.wasAcknowledgedSuccessfully).isFalse()
    }

    @Test
    fun `a message of an unhandled type is acknowledged without decoding it`() = testWithInvocationContext {

        val undecodable = UndecodableMessage<Event>(type = TestEvent2.TYPE)
        val handled = testEvent1().asMessage()
        val processed = mutableListOf<Event>()
        val processor = EventProcessor.withMessages(flowOf(undecodable, handled), handledTypes = setOf(TestEvent1.TYPE), processEvent = { message ->
            processed += message.value
            EventProcessingResult.Success
        })

        processor.start()
        handled.awaitSuccessfulAck()
        processor.stop()

        assertThat(undecodable.wasAcknowledged).isTrue()
        assertThat(processed).containsExactly(handled.value)
    }

    private val handledTypes = setOf(TestEvent1.TYPE, TestEvent2.TYPE)

    private fun <EVENT : Event> EVENT.asMessage() = ReceivedMessage.Companion.inMemorySpy(this, properties = mapOf(AcmeMessagePropertyNames.forEvents.type to type.stringValue))

    private inner class UndecodableMessage<EVENT : Event>(type: Happening.Type) : ReceivedMessage<EVENT> {

        var wasAcknowledged = false
        override val id = Message.Id.ulid(topic = Topic.create())
        override val key: String? = null
        override val value: EVENT get() = error("This payload cannot be decoded")
        override val rawData = byteArrayOf(0x00)
        override val properties = mapOf(AcmeMessagePropertyNames.forEvents.type to type.stringValue)
        override val context = Message.Context()
        override val producerName = Name.random()
        override val publishedAt = clock.now()

        override suspend fun acknowledge() {
            wasAcknowledged = true
        }
    }

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