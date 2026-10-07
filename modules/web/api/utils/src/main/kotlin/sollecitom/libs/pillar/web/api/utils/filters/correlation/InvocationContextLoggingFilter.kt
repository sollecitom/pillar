package sollecitom.libs.pillar.web.api.utils.filters.correlation

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import sollecitom.libs.pillar.correlation.logging.utils.toLoggingContext
import sollecitom.libs.swissknife.logger.core.withThreadLoggingContext
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys

/** Creates a filter that adds the invocation context to the coroutine logging MDC, so all log entries include correlation data. */
fun InvocationContextFilters.addInvocationContextToLoggingStack(): Filter = InvocationContextLoggingFilter()

internal class InvocationContextLoggingFilter : Filter {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val context = InvocationContextKeys.key.optional(request)
        if (context != null) {
            withThreadLoggingContext(context.toLoggingContext()) { next(request) }
        } else {
            next(request)
        }
    }
}

