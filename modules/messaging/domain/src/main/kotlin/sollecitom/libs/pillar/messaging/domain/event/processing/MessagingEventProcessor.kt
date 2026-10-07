package sollecitom.libs.pillar.messaging.domain.event.processing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.pillar.messaging.domain.message.processWithForkedContext
import sollecitom.libs.swissknife.core.domain.lifecycle.ProcessHalter
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.event.utils.eventType
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.connector.MessageConnector
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private class MessagingEventProcessor<in EVENT : Event>(
    private val handler: EventHandler<EVENT>,
    private val propertyNames: MessagePropertyNames,
    messages: Flow<ReceivedMessage<EVENT>>,
    scope: CoroutineScope,
    private val onFatalFailure: (Throwable) -> Unit,
    private val coreDataGenerator: CoreDataGenerator
) : EventProcessor, CoreDataGenerator by coreDataGenerator {

    init {
        require(handler.handledTypes.isNotEmpty()) { "An event processor must handle at least one event type" }
    }

    private val handledNames = handler.handledTypes.map { it.name }.toSet()

    val processing = scope.launch(start = LAZY) {
        try {
            messages.collect { message -> message.consume() }
        } catch (error: Throwable) {
            currentCoroutineContext().ensureActive()
            logger.error(error = error) { "Halting the event processor: ${error.message}" }
            onFatalFailure(error)
        }
    }

    private suspend fun ReceivedMessage<EVENT>.consume() = if (isHandled()) {
        ensureDecodable()
        retryingInPlace { processWithForkedContext { process(it) } }
    } else {
        retryingInPlace { acknowledge() }
    }

    private val ReceivedMessage<EVENT>.rawEventType get() = properties[propertyNames.forEvents.type]

    private fun ReceivedMessage<EVENT>.isHandled() = decoding { with(propertyNames) { eventType() }.name in handledNames }

    private fun ReceivedMessage<EVENT>.ensureDecodable() = decoding { value }

    private fun <RESULT> ReceivedMessage<EVENT>.decoding(action: () -> RESULT): RESULT = try {
        action()
    } catch (error: Throwable) {
        throw UndecodableMessageException(messageId = id, key = key, eventType = rawEventType, cause = error)
    }

    private suspend fun ReceivedMessage<EVENT>.retryingInPlace(action: suspend () -> Unit) {

        var retryDelay = minimumRetryDelay
        while (true) {
            try {
                return action()
            } catch (error: Throwable) {
                currentCoroutineContext().ensureActive()
                logger.error(error = error) { "Failed to process message with ID ${id.stringRepresentation}, key $key, and event type $rawEventType. Retrying it in $retryDelay" }
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(maximumRetryDelay)
            }
        }
    }

    context(_: InvocationContext<*>)
    private suspend fun process(message: ReceivedMessage<EVENT>) {

        handler(message)
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

private val haltingTheProcess: (Throwable) -> Unit = ProcessHalter.system::halt

/** Creates an [EventProcessor] that consumes events from a [MessageConnector], processing each with a forked invocation context. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    connector: MessageConnector<EVENT>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onFatalFailure: (Throwable) -> Unit = haltingTheProcess
): EventProcessor = withMessageConnector(CoroutineScope(SupervisorJob()), connector, handler, propertyNames, onFatalFailure)

/** Creates an [EventProcessor] that consumes events from a [MessageConnector] within the given [scope]. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    scope: CoroutineScope,
    connector: MessageConnector<EVENT>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onFatalFailure: (Throwable) -> Unit = haltingTheProcess
): EventProcessor = withMessages(connector.messages, handler, propertyNames, scope, onFatalFailure)

/** Creates an [EventProcessor] from a raw message [Flow], useful when not using a [MessageConnector]. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    scope: CoroutineScope = CoroutineScope(SupervisorJob()),
    onFatalFailure: (Throwable) -> Unit = haltingTheProcess
): EventProcessor = MessagingEventProcessor(handler = handler, propertyNames = propertyNames, messages = messages, scope = scope, onFatalFailure = onFatalFailure, coreDataGenerator = generator)

/** Creates an [EventProcessor] from a raw message [Flow], using the [CoroutineScope] from the context receiver. */
context(generator: CoreDataGenerator, scope: CoroutineScope)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onFatalFailure: (Throwable) -> Unit = haltingTheProcess
): EventProcessor = withMessages(messages, handler, propertyNames, scope, onFatalFailure)
