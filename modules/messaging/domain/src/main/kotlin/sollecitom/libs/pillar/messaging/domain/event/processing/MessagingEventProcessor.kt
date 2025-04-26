package sollecitom.libs.pillar.messaging.domain.event.processing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import sollecitom.libs.pillar.messaging.domain.message.processWithForkedContext
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.event.processing.EventProcessingResult.*
import sollecitom.libs.swissknife.messaging.domain.event.processing.ProcessEvent
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.connector.MessageConnector

private class MessagingEventProcessor<in EVENT : Event>(
    private val processEvent: ProcessEvent<EVENT>,
    messages: Flow<ReceivedMessage<EVENT>>,
    scope: CoroutineScope,
    private val coreDataGenerator: CoreDataGenerator
) : EventProcessor, CoreDataGenerator by coreDataGenerator {

    val processing = messages.processWithForkedContext(start = LAZY, scope = scope) { message ->

        logger.info { "Received message with ID ${message.id.stringRepresentation}, key: ${message.key}, and value ${message.value}" }
        when (val result = processEvent(message)) {
            is Success -> {
                logger.info { "Successfully processed message with ID ${message.id.stringRepresentation}, key: ${message.key}, and value ${message.value}" }
                message.acknowledge()
            }

            is NoOp -> {
                logger.info { "Ignored message with ID with ID ${message.id.stringRepresentation}, key: ${message.key}, and value ${message.value}" }
                message.acknowledge()
            }

            is Failure -> {
                logger.warn(error = result.error) { "Failed to process message with ID ${message.id.stringRepresentation}, key ${message.key}, and value ${message.value}" }
                message.acknowledgeAsFailed()
            }
        }
    }

    override suspend fun start() {
        processing.start()
    }

    override suspend fun stop() = processing.cancelAndJoin()

    companion object : Loggable()
}

context(CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessageConnector(
    connector: MessageConnector<EVENT>,
    processEvent: ProcessEvent<EVENT>,
    scope: CoroutineScope = CoroutineScope(Job()) // TODO should this be SupervisorJob()?
): EventProcessor = MessagingEventProcessor(
    processEvent = processEvent,
    messages = connector.messages,
    scope = scope,
    coreDataGenerator = this@CoreDataGenerator
)

context(CoreDataGenerator)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    processEvent: ProcessEvent<EVENT>,
    scope: CoroutineScope = CoroutineScope(Job())
): EventProcessor = MessagingEventProcessor(
    processEvent = processEvent,
    messages = messages,
    scope = scope,
    coreDataGenerator = this@CoreDataGenerator
)

context(CoreDataGenerator, CoroutineScope)
fun <EVENT : Event> EventProcessor.Companion.withMessages(
    messages: Flow<ReceivedMessage<EVENT>>,
    processEvent: ProcessEvent<EVENT>,
): EventProcessor = MessagingEventProcessor(
    messages = messages,
    scope = this@CoroutineScope,
    coreDataGenerator = this@CoreDataGenerator,
    processEvent = processEvent
)