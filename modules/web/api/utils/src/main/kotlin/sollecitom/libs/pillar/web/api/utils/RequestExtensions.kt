package sollecitom.libs.pillar.web.api.utils

import org.http4k.core.Request
import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.withInvocationContext

/** Attaches the [context] as a JSON-serialized header with the given [headerName] to this request. */
fun Request.withInvocationContext(headerName: String, context: InvocationContext<*>): Request = withInvocationContext(headerName, context) { ctx -> InvocationContext.jsonSerde.serialize(ctx).toString() }