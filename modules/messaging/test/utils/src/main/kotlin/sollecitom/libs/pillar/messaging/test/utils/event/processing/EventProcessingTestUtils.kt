package sollecitom.libs.pillar.messaging.test.utils.event.processing

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import sollecitom.libs.pillar.messaging.domain.event.processing.EventHandler
import sollecitom.libs.pillar.messaging.domain.event.processing.withMessages
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.messaging.test.utils.message.ReceivedMessageSpy
import sollecitom.libs.swissknife.messaging.test.utils.message.waitUntilAllAcked

context(_: CoreDataGenerator)
suspend fun <EVENT : Event> List<ReceivedMessageSpy<out EVENT>>.processAndWaitUntilAllAcked(handler: EventHandler<EVENT>) = coroutineScope {

    val processor = EventProcessor.withMessages(asFlow(), handler)
    processor.start()
    waitUntilAllAcked()
    processor.stop()
}
