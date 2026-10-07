package sollecitom.libs.pillar.web.api.utils.endpoint

import sollecitom.libs.pillar.json.serialization.web.api.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.authenticatedOrNull
import sollecitom.libs.swissknife.correlation.core.domain.context.customerOrNull
import sollecitom.libs.swissknife.correlation.core.domain.context.unauthenticatedOrNull
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import sollecitom.libs.swissknife.web.api.domain.error.ErrorCode
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler

/** Routes to an action that requires an authenticated invocation context. Returns 422 if the context is not authenticated. */
infix fun PathMethod.toAuthenticated(action: suspend InvocationContext<Access.Authenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextKeys.key.mandatory(request)
    val authenticated = context.authenticatedOrNull()
    if (authenticated == null) {
        apiError(error = ApiError(code = ErrorCode.AuthenticatedAccessRequired))
    } else {
        runBlocking(MDCContext()) { with(authenticated) { action(request) } }
    }
}

infix fun PathMethod.toCustomerScoped(action: suspend InvocationContext<Access.Authenticated>.(request: Request, customer: Customer) -> Response): RoutingHttpHandler = toAuthenticated { request ->

    when (val customer = customerOrNull) {
        null -> apiError(error = ApiError(code = ErrorCode.CustomerRequired), status = Status.FORBIDDEN)
        else -> action(request, customer)
    }
}

/** Routes to an action that requires an unauthenticated invocation context. Returns 422 if the context is authenticated. */
infix fun PathMethod.toUnauthenticated(action: suspend InvocationContext<Access.Unauthenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextKeys.key.mandatory(request)
    val unauthenticated = context.unauthenticatedOrNull()
    if (unauthenticated == null) {
        apiError(error = ApiError(code = ErrorCode.UnauthenticatedAccessRequired))
    } else {
        runBlocking(MDCContext()) { with(unauthenticated) { action(request) } }
    }
}

/** Routes to an action with the invocation context available, regardless of authentication status. */
infix fun PathMethod.toWithInvocationContext(action: suspend InvocationContext<Access>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextKeys.key.mandatory(request)
    runBlocking(MDCContext()) { with(context) { action(request) } }
}

private fun apiError(error: ApiError, status: Status = Status.UNPROCESSABLE_ENTITY) = Response(status = status).body(error, ApiError.jsonSerde)