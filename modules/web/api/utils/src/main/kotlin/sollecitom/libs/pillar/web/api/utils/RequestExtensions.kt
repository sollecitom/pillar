package sollecitom.libs.pillar.web.api.utils

import org.http4k.core.Request
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.web.api.utils.withInvocationContext

/** Attaches the [context] as a header with the given [headerName] to this request, encoded by [toHeaderValue]. */
fun Request.withInvocationContext(headerName: String, context: InvocationContext<*>): Request = withInvocationContext(headerName, context) { it.toHeaderValue() }