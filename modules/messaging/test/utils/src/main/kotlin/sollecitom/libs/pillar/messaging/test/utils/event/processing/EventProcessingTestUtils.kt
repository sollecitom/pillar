package sollecitom.libs.pillar.messaging.test.utils.event.processing

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.flow.asFlow
import sollecitom.libs.pillar.messaging.domain.event.processing.EventHandler
import sollecitom.libs.pillar.messaging.domain.event.processing.UndecodableMessageException
import sollecitom.libs.pillar.messaging.domain.event.processing.withMessages
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.EventProcessor
import sollecitom.libs.swissknife.messaging.test.utils.message.ReceivedMessageSpy
import sollecitom.libs.swissknife.messaging.test.utils.message.waitUntilAllAcked

context(_: CoreDataGenerator)
suspend fun <EVENT : Event> List<ReceivedMessageSpy<out EVENT>>.processAndWaitUntilAllAcked(handler: EventHandler<EVENT>) = coroutineScope {

    val halted = CompletableDeferred<UndecodableMessageException>()
    val processor = EventProcessor.withMessages(asFlow(), handler, onUndecodableMessage = halted::complete)
    processor.start()
    val allAcked = async { waitUntilAllAcked() }
    try {
        select { allAcked.onAwait {}; halted.onAwait { throw it } }
    } finally {
        allAcked.cancel()
        processor.stop()
    }
}
