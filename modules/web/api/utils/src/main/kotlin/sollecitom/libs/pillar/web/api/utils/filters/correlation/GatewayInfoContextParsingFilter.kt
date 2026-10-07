package sollecitom.libs.pillar.web.api.utils.filters.correlation

import sollecitom.libs.pillar.web.api.utils.fromHeaderValue
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.forked
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import org.http4k.core.*
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextKeys

/** Creates a filter that parses the invocation context from a gateway-forwarded header and forks it for the current invocation. */
context(generator: CoreDataGenerator)
fun InvocationContextFilters.parseInvocationContextFromGatewayHeader(headerNames: HttpHeaderNames.Correlation): Filter = GatewayInfoContextParsingFilter(InvocationContextKeys.key, headerNames, generator)

/** Creates a filter that parses the invocation context from a gateway-forwarded header, using the API definition's header names. */
context(api: HttpApiDefinition, _: CoreDataGenerator)
fun InvocationContextFilters.parseInvocationContextFromGatewayHeader(): Filter = parseInvocationContextFromGatewayHeader(api.headerNames.correlation)

internal class GatewayInfoContextParsingFilter(private val key: InvocationContextKeys.Key, private val headerNames: HttpHeaderNames.Correlation, coreDataGenerator: CoreDataGenerator) : Filter, CoreDataGenerator by coreDataGenerator {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val attempt = runCatching { invocationContext(request, headerNames)?.forked() }
        when {
            attempt.isSuccess -> request.with(attempt.getOrThrow()).let(next)
            else -> attempt.exceptionOrNull()!!.asResponse()
        }
    }

    private fun Request.with(context: InvocationContext<*>?): Request = when (context) {
        null -> with(key.optional of null)
        else -> with(key.mandatory of context, key.optional of context)
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: ${message?.replace(whitespace, " ")}"))

    private fun invocationContext(request: Request, headerNames: HttpHeaderNames.Correlation): InvocationContext<Access>? {

        val rawValue = request.rawInvocationContextValue(headerNames) ?: return null
        return runCatching { InvocationContext.fromHeaderValue(rawValue) }.getOrElse { cause -> error("Invalid value for header ${headerNames.invocationContext}, which must be the base64url encoding of an invocation context JSON object. ${cause.message}") }
    }

    private fun Request.rawInvocationContextValue(headerNames: HttpHeaderNames.Correlation): String? {

        val invocationContextValues = headerValues(headerNames.invocationContext)
        check(invocationContextValues.size <= 1) { "Multiple values for header ${headerNames.invocationContext}. At most one is allowed." }
        return invocationContextValues.singleOrNull().takeUnless { it.isNullOrBlank() }
    }

    private companion object {
        val whitespace = Regex("\\s+")
    }
}
