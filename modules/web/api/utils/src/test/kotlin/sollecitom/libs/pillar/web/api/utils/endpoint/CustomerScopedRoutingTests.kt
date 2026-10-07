package sollecitom.libs.pillar.web.api.utils.endpoint

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.internalService
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys

@TestInstance(PER_CLASS)
class CustomerScopedRoutingTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val handler = routes("/things" bind GET toCustomerScoped { Response(OK) })

    @Test
    fun `an invocation with a customer reaches the action`() {

        val context = InvocationContext.authenticated()

        val response = handler(context.asRequest())

        assertThat(response.status).isEqualTo(OK)
    }

    @Test
    fun `an invocation without a customer is forbidden`() {

        val context = InvocationContext.authenticated(access = { Access.authenticated(actor = Actor.direct(account = Actor.Account.internalService())) })

        val response = handler(context.asRequest())

        assertThat(response.status).isEqualTo(Status.FORBIDDEN)
    }

    private fun InvocationContext<*>.asRequest() = Request(GET, "/things").with(InvocationContextKeys.key.mandatory of this, InvocationContextKeys.key.optional of this)
}
