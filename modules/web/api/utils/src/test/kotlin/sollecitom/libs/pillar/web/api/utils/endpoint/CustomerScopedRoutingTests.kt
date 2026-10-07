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
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.web.api.jsonSerde
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.internalService
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.user
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys

@TestInstance(PER_CLASS)
class CustomerScopedRoutingTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val handler = routes("/things" bind GET toCustomerScoped { _, customer -> Response(OK).body(customer.id.stringValue) })

    @Test
    fun `an invocation with a customer reaches the action with that customer`() {

        val customer = Customer.create()
        val context = InvocationContext.authenticated(access = { Access.authenticated(actor = Actor.direct(account = Actor.Account.user(customer = customer))) })

        val response = context.asRequest().let(handler)

        assertThat(response.status).isEqualTo(OK)
        assertThat(response.bodyString()).isEqualTo(customer.id.stringValue)
    }

    @Test
    fun `an invocation without an actor customer but with a specified target customer reaches the action with the target customer`() {

        val targetCustomer = Customer.create()
        val context = InvocationContext.authenticated(access = { Access.authenticated(actor = Actor.direct(account = Actor.Account.internalService())) }, specifiedTargetCustomer = { targetCustomer })

        val response = context.asRequest().let(handler)

        assertThat(response.status).isEqualTo(OK)
        assertThat(response.bodyString()).isEqualTo(targetCustomer.id.stringValue)
    }

    @Test
    fun `an invocation without a customer is forbidden`() {

        val context = InvocationContext.authenticated(access = { Access.authenticated(actor = Actor.direct(account = Actor.Account.internalService())) })

        val response = context.asRequest().let(handler)

        assertThat(response.status).isEqualTo(Status.FORBIDDEN)
        assertThat(response.bodyString().let(::JSONObject).let(ApiError.jsonSerde::deserialize)).isEqualTo(ApiError(message = "The invocation requires a customer to scope it to", code = "01K70Q5Z3S9V2XG8M4T6HJ1RNC"))
    }

    @Test
    fun `a specified target customer takes precedence over the actor's customer`() {

        val actorCustomer = Customer.create()
        val targetCustomer = Customer.create()
        val context = InvocationContext.authenticated(access = { Access.authenticated(actor = Actor.direct(account = Actor.Account.user(customer = actorCustomer))) }, specifiedTargetCustomer = { targetCustomer })

        val response = context.asRequest().let(handler)

        assertThat(response.status).isEqualTo(OK)
        assertThat(response.bodyString()).isEqualTo(targetCustomer.id.stringValue)
    }

    private fun InvocationContext<*>.asRequest() = Request(GET, "/things").with(InvocationContextKeys.key.mandatory of this, InvocationContextKeys.key.optional of this)
}
