package sollecitom.libs.pillar.web.api.utils.api

import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import org.http4k.core.Request
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.withInvocationContext
import kotlin.toString

/** Attaches the [context] as a JSON-serialized header using the API definition's header names. */
context(_: HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>): Request = withInvocationContext(context) { ctx -> InvocationContext.jsonSerde.serialize(ctx).toString() }

/** Attaches the invocation context from the context receiver as a JSON-serialized header. */
context(_: HttpApiDefinition, context: InvocationContext<*>)
fun Request.withInvocationContext(): Request = withInvocationContext(context)