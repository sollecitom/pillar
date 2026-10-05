package sollecitom.libs.pillar.web.api.utils.api

import sollecitom.libs.pillar.web.api.utils.toHeaderValue
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import org.http4k.core.Request
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.withInvocationContext

/** Attaches the [context] as a header using the API definition's header names, encoded by [toHeaderValue]. */
context(_: HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>): Request = withInvocationContext(context) { it.toHeaderValue() }

/** Attaches the invocation context from the context receiver as a header, encoded by [toHeaderValue]. */
context(_: HttpApiDefinition, context: InvocationContext<*>)
fun Request.withInvocationContext(): Request = withInvocationContext(context)