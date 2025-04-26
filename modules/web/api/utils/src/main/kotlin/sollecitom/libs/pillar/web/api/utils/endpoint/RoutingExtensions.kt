package sollecitom.libs.pillar.web.api.utils.endpoint

import sollecitom.libs.pillar.json.serialization.web.api.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.authenticatedOrNull
import sollecitom.libs.swissknife.correlation.core.domain.context.unauthenticatedOrNull
import sollecitom.libs.swissknife.http4k.utils.body
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import sollecitom.libs.swissknife.web.api.domain.error.ErrorCode
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter
import kotlinx.coroutines.runBlocking
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler

infix fun PathMethod.toAuthenticated(action: suspend InvocationContext<Access.Authenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.mandatory(request)
    val authenticated = context.authenticatedOrNull()
    if (authenticated == null) {
        apiError(code = ErrorCode.AuthenticatedAccessRequired)
    } else {
        runBlocking { with(authenticated) { action(request) } }
    }
}

infix fun PathMethod.toUnauthenticated(action: suspend InvocationContext<Access.Unauthenticated>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.mandatory(request)
    val unauthenticated = context.unauthenticatedOrNull()
    if (unauthenticated == null) {
        apiError(code = ErrorCode.UnauthenticatedAccessRequired)
    } else {
        runBlocking { with(unauthenticated) { action(request) } }
    }
}

infix fun PathMethod.toWithInvocationContext(action: suspend InvocationContext<Access>.(request: Request) -> Response): RoutingHttpHandler = to { request ->

    val context = InvocationContextFilter.key.mandatory(request)
    runBlocking { with(context) { action(request) } }
}

private fun apiError(code: ErrorCode): Response {

    val error = ApiError(code = code)
    return Response(status = Status.UNPROCESSABLE_ENTITY).body(error, ApiError.jsonSerde)
}