package sollecitom.libs.pillar.messaging.domain.message

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sollecitom.libs.pillar.correlation.logging.utils.withLoggingContext
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.forkAndLogInvocationContext
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage

/** Launches a coroutine that processes each message with a forked invocation context and structured logging. Uses the coroutine scope from the context receiver. */
context(scope: CoroutineScope, _: UniqueIdGenerator, _: TimeGenerator, _: Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.processWithForkedContext(start: CoroutineStart = LAZY, action: suspend context(InvocationContext<*>) (message: ReceivedMessage<EVENT>) -> Unit): Job = processWithForkedContext(scope, start, action)

/** Launches a coroutine in the given [scope] that processes each message with a forked invocation context and structured logging. */
context(_: UniqueIdGenerator, _: TimeGenerator, _: Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.processWithForkedContext(scope: CoroutineScope, start: CoroutineStart = LAZY, action: suspend context(InvocationContext<*>) (message: ReceivedMessage<EVENT>) -> Unit): Job = scope.launch(start = start) {
    onEach { message ->
        val invocationContext = message.value.forkAndLogInvocationContext()
        withLoggingContext(invocationContext) {
            action(invocationContext, message)
        }
    }.collect()
}