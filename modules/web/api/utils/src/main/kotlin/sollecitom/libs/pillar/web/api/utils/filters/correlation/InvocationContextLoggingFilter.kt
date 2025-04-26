package sollecitom.libs.pillar.web.api.utils.filters.correlation

import kotlinx.coroutines.runBlocking
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import sollecitom.libs.pillar.correlation.logging.utils.toLoggingContext
import sollecitom.libs.swissknife.logger.core.withCoroutineLoggingContext
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter

fun InvocationContextFilter.addInvocationContextToLoggingStack(): Filter = InvocationContextLoggingFilter()

internal class InvocationContextLoggingFilter : Filter {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val context = InvocationContextFilter.key.optional(request)
        if (context != null) {
            runBlocking {
                withCoroutineLoggingContext(context.toLoggingContext()) {
                    next(request)
                }
            }
        } else {
            next(request)
        }
    }
}

