package sollecitom.libs.pillar.messaging.domain.event.processing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sollecitom.libs.pillar.messaging.domain.message.processWithForkedContext
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.messaging.domain.event.utils.eventType
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.event.processing.ProcessEvent
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.connector.MessageConnector
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private class MessagingEventProcessor<in EVENT : Event>(
    private val handledTypes: Set<Happening.Type>,
    private val processEvent: ProcessEvent<EVENT>,
    private val propertyNames: MessagePropertyNames,
    messages: Flow<ReceivedMessage<EVENT>>,
    scope: CoroutineScope,
    private val coreDataGenerator: CoreDataGenerator
) : EventProcessor, CoreDataGenerator by coreDataGenerator {

    val processing = scope.launch(start = LAZY) {
        messages.collect { message -> processUntilSuccessful(message) }
    }

    private suspend fun processUntilSuccessful(message: ReceivedMessage<EVENT>) {

        var retryDelay = minimumRetryDelay
        while (true) {
            try {
                return if (message.isHandled()) message.processWithForkedContext { process(it) } else message.acknowledge()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                logger.error(error = error) { "Failed to process message with ID ${message.id.stringRepresentation}, key ${message.key}, and value ${message.value}. Retrying it in $retryDelay" }
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(maximumRetryDelay)
            }
        }
    }

    private fun ReceivedMessage<EVENT>.isHandled() = with(propertyNames) { eventType() in handledTypes }

    context(_: InvocationContext<*>)
    private suspend fun process(message: ReceivedMessage<EVENT>) {

        processEvent(message)
        message.acknowledge()
    }

    override suspend fun start() {
        processing.start()
    }

    override suspend fun stop() = processing.cancelAndJoin()

    companion object : Loggable() {
        private val minimumRetryDelay = 1.seconds
        private val maximumRetryDelay = 1.minutes
    }
}

/**
 * Creates an [EventProcessor] that consumes events from a [MessageConnector], processing each with a forked invocation context.
 * Messages whose event type isn't in [handledTypes] are acknowledged without being decoded.
 */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    connector: MessageConnector<EVENT>,
    handledTypes: Set<Happening.Type>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    processEvent: ProcessEvent<EVENT>,
): EventProcessor = withMessageConnector(
    CoroutineScope(SupervisorJob()),
    connector,
    handledTypes,
    propertyNames,
    processEvent
)

/** Creates an [EventProcessor] that consumes events from a [MessageConnector] within the given [scope]. Messages whose event type isn't in [handledTypes] are acknowledged without being decoded. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    scope: CoroutineScope,
    connector: MessageConnector<EVENT>,
    handledTypes: Set<Happening.Type>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    processEvent: ProcessEvent<EVENT>,
): EventProcessor = MessagingEventProcessor(
    handledTypes = handledTypes,
    processEvent = processEvent,
    propertyNames = propertyNames,
    messages = connector.messages,
    scope = scope,
    coreDataGenerator = generator
)

/** Creates an [EventProcessor] from a raw message [Flow], useful when not using a [MessageConnector]. Messages whose event type isn't in [handledTypes] are acknowledged without being decoded. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handledTypes: Set<Happening.Type>,
    processEvent: ProcessEvent<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    scope: CoroutineScope = CoroutineScope(SupervisorJob())
): EventProcessor = MessagingEventProcessor(
    handledTypes = handledTypes,
    processEvent = processEvent,
    propertyNames = propertyNames,
    messages = messages,
    scope = scope,
    coreDataGenerator = generator
)

/** Creates an [EventProcessor] from a raw message [Flow], using the [CoroutineScope] from the context receiver. Messages whose event type isn't in [handledTypes] are acknowledged without being decoded. */
context(generator: CoreDataGenerator, scope: CoroutineScope)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handledTypes: Set<Happening.Type>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    processEvent: ProcessEvent<EVENT>,
): EventProcessor = MessagingEventProcessor(
    handledTypes = handledTypes,
    propertyNames = propertyNames,
    messages = messages,
    scope = scope,
    coreDataGenerator = generator,
    processEvent = processEvent
)