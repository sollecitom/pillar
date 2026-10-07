package sollecitom.libs.pillar.messaging.domain.event.processing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
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
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private class MessagingEventProcessor<in EVENT : Event>(
    private val handler: EventHandler<EVENT>,
    private val propertyNames: MessagePropertyNames,
    messages: Flow<ReceivedMessage<EVENT>>,
    scope: CoroutineScope,
    private val onUndecodableMessage: (UndecodableMessageException) -> Unit,
    private val coreDataGenerator: CoreDataGenerator
) : EventProcessor, CoreDataGenerator by coreDataGenerator {

    init {
        require(handler.handledTypes.isNotEmpty()) { "An event processor must handle at least one event type" }
    }

    private val handledNames = handler.handledTypes.map { it.name }.toSet()

    val processing = scope.launch(start = LAZY) {
        try {
            messages.collect { message -> message.consume() }
        } catch (error: UndecodableMessageException) {
            onUndecodableMessage(error)
        }
    }

    private suspend fun ReceivedMessage<EVENT>.consume() = if (isHandled()) {
        ensureDecodable()
        retryingInPlace { processWithForkedContext { process(it) } }
    } else {
        retryingInPlace { acknowledge() }
    }

    private fun ReceivedMessage<EVENT>.isHandled() = decoding { with(propertyNames) { eventType() }.name in handledNames }

    private fun ReceivedMessage<EVENT>.ensureDecodable() = decoding { value }

    private fun <RESULT> ReceivedMessage<EVENT>.decoding(action: () -> RESULT): RESULT = try {
        action()
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        throw UndecodableMessageException(messageId = id, key = key, eventType = properties[propertyNames.forEvents.type], cause = error)
    }

    private suspend fun ReceivedMessage<EVENT>.retryingInPlace(action: suspend () -> Unit) {

        var retryDelay = minimumRetryDelay
        while (true) {
            try {
                return action()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                logger.error(error = error) { "Failed to process message with ID ${id.stringRepresentation}, key $key, and event type ${properties[propertyNames.forEvents.type]}. Retrying it in $retryDelay" }
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

/** Creates an [EventProcessor] that consumes events from a [MessageConnector], processing each with a forked invocation context. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    connector: MessageConnector<EVENT>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onUndecodableMessage: (UndecodableMessageException) -> Unit = ProcessHalter.system::halt
): EventProcessor = withMessageConnector(CoroutineScope(SupervisorJob()), connector, handler, propertyNames, onUndecodableMessage)

/** Creates an [EventProcessor] that consumes events from a [MessageConnector] within the given [scope]. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    scope: CoroutineScope,
    connector: MessageConnector<EVENT>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onUndecodableMessage: (UndecodableMessageException) -> Unit = ProcessHalter.system::halt
): EventProcessor = MessagingEventProcessor(handler = handler, propertyNames = propertyNames, messages = connector.messages, scope = scope, onUndecodableMessage = onUndecodableMessage, coreDataGenerator = generator)

/** Creates an [EventProcessor] from a raw message [Flow], useful when not using a [MessageConnector]. */
context(generator: CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    scope: CoroutineScope = CoroutineScope(SupervisorJob()),
    onUndecodableMessage: (UndecodableMessageException) -> Unit = ProcessHalter.system::halt
): EventProcessor = MessagingEventProcessor(handler = handler, propertyNames = propertyNames, messages = messages, scope = scope, onUndecodableMessage = onUndecodableMessage, coreDataGenerator = generator)

/** Creates an [EventProcessor] from a raw message [Flow], using the [CoroutineScope] from the context receiver. */
context(generator: CoreDataGenerator, scope: CoroutineScope)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    handler: EventHandler<EVENT>,
    propertyNames: MessagePropertyNames = AcmeMessagePropertyNames,
    onUndecodableMessage: (UndecodableMessageException) -> Unit = ProcessHalter.system::halt
): EventProcessor = MessagingEventProcessor(handler = handler, propertyNames = propertyNames, messages = messages, scope = scope, onUndecodableMessage = onUndecodableMessage, coreDataGenerator = generator)
