package sollecitom.libs.pillar.web.api.utils.filters.correlation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.slf4j.MDC
import sollecitom.libs.pillar.correlation.logging.utils.toLoggingContext
import sollecitom.libs.pillar.web.api.utils.endpoint.toAuthenticated
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys

@TestInstance(PER_CLASS)
class InvocationContextLoggingFilterTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `the invocation context is in the logging context of a handler running on another dispatcher`() {

        val context = InvocationContext.authenticated()
        var loggingContextInHandler: Map<String, String>? = null
        val handler = InvocationContextFilters.addInvocationContextToLoggingStack().then(routes("/things" bind GET toAuthenticated {
            loggingContextInHandler = withContext(Dispatchers.Default) { MDC.getCopyOfContextMap() }
            Response(OK)
        }))

        Request(GET, "/things").with(InvocationContextKeys.key.mandatory of context, InvocationContextKeys.key.optional of context).let(handler)

        assertThat(loggingContextInHandler).isEqualTo(context.toLoggingContext())
    }

    @Test
    fun `the logging context is cleared once the request is handled`() {

        val context = InvocationContext.authenticated()
        val handler = InvocationContextFilters.addInvocationContextToLoggingStack().then { Response(OK) }

        Request(GET, "/things").with(InvocationContextKeys.key.mandatory of context, InvocationContextKeys.key.optional of context).let(handler)

        assertThat(MDC.getCopyOfContextMap().orEmpty()).isEmpty()
    }
}
