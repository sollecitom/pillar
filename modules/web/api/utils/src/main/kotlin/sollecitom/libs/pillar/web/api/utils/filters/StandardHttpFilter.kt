package sollecitom.libs.pillar.web.api.utils.filters

import io.micrometer.core.instrument.MeterRegistry
import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.MicrometerMetrics
import org.http4k.filter.RequestFilters
import org.http4k.filter.ResponseFilters
import org.http4k.filter.ServerFilters
import org.http4k.filter.inIntelliJOnly
import org.http4k.lens.LensFailure
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.http4k.utils.AddContentLength
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.filters.catchAndLogErrors
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter
import sollecitom.libs.pillar.web.api.utils.filters.correlation.addInvocationContextToLoggingStack
import sollecitom.libs.pillar.web.api.utils.filters.correlation.parseInvocationContextFromGatewayHeader

object StandardHttpFilter : Loggable() {

    context(_: HttpApiDefinition, time: CoreDataGenerator)
    fun forRequests(meterRegistry: MeterRegistry): Filter = ServerFilters.catchAndLogErrors
        .then(
            ServerFilters.CatchLensFailure { request: Request, lensFailure: LensFailure ->
                val errorDescription = lensFailure.failures.joinToString("; ")
                logger.info { "Validation failure while processing a request with method ${request.method} on path '${request.uri}'. The error was: '$errorDescription'" }
                Response.Companion(Status.Companion.BAD_REQUEST.description(errorDescription))
            }
        )
        .then(ServerFilters.MicrometerMetrics.RequestTimer(meterRegistry = meterRegistry, clock = time.javaClock))
        .then(RequestFilters.GunZip())
        .then(DebuggingFilters.PrintRequestAndResponse().inIntelliJOnly())
        .then(InvocationContextFilter.parseInvocationContextFromGatewayHeader())
        .then(InvocationContextFilter.addInvocationContextToLoggingStack())

    fun forResponses(): Filter = ResponseFilters.GZip().then(
        ResponseFilters.AddContentLength
    )
}