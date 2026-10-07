package sollecitom.libs.pillar.messaging.test.utils.event.processing

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import sollecitom.libs.pillar.messaging.domain.event.processing.withMessages
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.messaging.domain.event.processing.ProcessEvent
import sollecitom.libs.swissknife.messaging.test.utils.message.ReceivedMessageSpy
import sollecitom.libs.swissknife.messaging.test.utils.message.waitUntilAllAcked

context(_: CoreDataGenerator)
suspend fun <EVENT : Event> List<ReceivedMessageSpy<out EVENT>>.processAndWaitUntilAllAcked(handledTypes: Set<Happening.Type> = map { it.value.type }.toSet(), processEvent: ProcessEvent<EVENT>) = coroutineScope {

    val processor = EventProcessor.withMessages(asFlow(), handledTypes, processEvent)
    processor.start()
    waitUntilAllAcked()
    processor.stop()
}