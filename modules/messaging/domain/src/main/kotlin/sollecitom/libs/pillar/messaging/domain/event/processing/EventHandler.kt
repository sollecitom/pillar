package sollecitom.libs.pillar.messaging.domain.event.processing

import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.messaging.domain.event.processing.ProcessEvent
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage

interface EventHandler<in EVENT : Event> : ProcessEvent<EVENT> {

    val handledTypes: Set<Happening.Type>

    companion object
}

fun <EVENT : Event> EventHandler.Companion.byType(handlers: Map<Happening.Type, ProcessEvent<EVENT>>): EventHandler<EVENT> = TypeDispatchingEventHandler(handlers)

private class TypeDispatchingEventHandler<in EVENT : Event>(private val handlers: Map<Happening.Type, ProcessEvent<EVENT>>) : EventHandler<EVENT> {

    override val handledTypes = handlers.keys

    context(_: InvocationContext<*>)
    override suspend fun invoke(event: ReceivedMessage<EVENT>) = handlers.getValue(event.value.type)(event)
}
