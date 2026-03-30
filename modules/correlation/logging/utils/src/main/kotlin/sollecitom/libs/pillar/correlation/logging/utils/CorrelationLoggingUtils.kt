package sollecitom.libs.pillar.correlation.logging.utils

import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.logging.utils.toLoggingContext
import sollecitom.libs.swissknife.correlation.logging.utils.withLoggingContext

/** Converts this invocation context to a map of MDC key-value pairs for structured logging, using JSON serialization. */
fun InvocationContext<*>.toLoggingContext(): Map<String, String> = toLoggingContext { context -> InvocationContext.jsonSerde.serialize(context).toString() }

/** Executes [action] within a coroutine logging context populated with the invocation context's correlation data. */
suspend inline fun <ACCESS : Access, T> withLoggingContext(invocationContext: InvocationContext<ACCESS>, crossinline action: suspend context(InvocationContext<*>) () -> T): T = withLoggingContext(invocationContext, { context -> InvocationContext.jsonSerde.serialize(context).toString() }, action)