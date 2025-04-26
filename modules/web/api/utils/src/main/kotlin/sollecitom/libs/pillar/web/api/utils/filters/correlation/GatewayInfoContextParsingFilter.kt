package sollecitom.libs.pillar.web.api.utils.filters.correlation

import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.forked
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import org.http4k.core.*
import org.json.JSONObject
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter

context(generator: CoreDataGenerator)
fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(headerNames: HttpHeaderNames.Correlation): Filter = GatewayInfoContextParsingFilter(key, headerNames, generator)

context(api: HttpApiDefinition, _: CoreDataGenerator)
fun InvocationContextFilter.parseInvocationContextFromGatewayHeader(): Filter = parseInvocationContextFromGatewayHeader(api.headerNames.correlation)

internal class GatewayInfoContextParsingFilter(private val key: InvocationContextFilter.Key, private val headerNames: HttpHeaderNames.Correlation, coreDataGenerator: CoreDataGenerator) : Filter, CoreDataGenerator by coreDataGenerator {

    override fun invoke(next: HttpHandler) = { request: Request ->

        val attempt = runCatching { invocationContext(request, headerNames)?.forked() }
        when {
            attempt.isSuccess -> next(request.with(attempt.getOrThrow()))
            else -> attempt.exceptionOrNull()!!.asResponse()
        }
    }

    private fun Request.with(context: InvocationContext<*>?): Request = when (context) {
        null -> with(key.optional of null)
        else -> with(key.mandatory of context, key.optional of context)
    }

    private fun Throwable.asResponse() = Response(Status.BAD_REQUEST.description("Error while parsing the invocation context: $message"))

    private fun invocationContext(request: Request, headerNames: HttpHeaderNames.Correlation): InvocationContext<Access>? {

        val rawValue = request.rawInvocationContextValue(headerNames) ?: return null
        val jsonValue = runCatching { JSONObject(rawValue) }.getOrElse { error("Invalid value for header ${headerNames.invocationContext}. Must be a JSON object.") }
        return InvocationContext.jsonSerde.deserialize(jsonValue) // TODO this should be forked!
    }

    private fun Request.rawInvocationContextValue(headerNames: HttpHeaderNames.Correlation): String? {

        val invocationContextHeaders = headers.filter { it.first == headerNames.invocationContext }.takeUnless { it.isEmpty() } ?: return null
        return when {
            invocationContextHeaders.size > 1 -> invocationContextHeaders.joinToString(separator = ",") { it.second!! }
            else -> invocationContextHeaders.single().second.takeUnless { it.isNullOrBlank() }
        }
    }
}