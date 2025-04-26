package sollecitom.libs.pillar.web.api.utils.filters.correlation

import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.jwt.domain.JwtParty
import sollecitom.libs.swissknife.jwt.domain.JwtProcessor
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.filters.catchAndLogErrors
import sollecitom.libs.swissknife.web.api.utils.filters.correlation.InvocationContextFilter
import io.micrometer.core.instrument.MeterRegistry
import org.http4k.core.*
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.MicrometerMetrics
import org.http4k.filter.ServerFilters
import org.http4k.filter.ServerFilters.CatchLensFailure
import org.http4k.filter.inIntelliJOnly
import org.http4k.lens.LensFailure

object GatewayHttpFilter : Loggable() {

    context(_: HttpApiDefinition, time: CoreDataGenerator)
    fun forRequests(meterRegistry: MeterRegistry, jwtProcessorConfiguration: JwtProcessor.Configuration, issuerForDomain: (String) -> JwtParty): Filter = ServerFilters.catchAndLogErrors
        .then(
            CatchLensFailure { request: Request, lensFailure: LensFailure ->
                val errorDescription = lensFailure.failures.joinToString("; ")
                logger.info { "Validation failure while processing a request with method ${request.method} on path '${request.uri}'. The error was: '$errorDescription'" }
                Response(BAD_REQUEST.description(errorDescription))
            }
        )
        .then(ServerFilters.MicrometerMetrics.RequestTimer(meterRegistry = meterRegistry, clock = time.javaClock))
        .then(DebuggingFilters.PrintRequestAndResponse().inIntelliJOnly())
        .then(InvocationContextFilter.parseInvocationContextFromRequest(issuerForDomain = issuerForDomain, jwtProcessorConfiguration = jwtProcessorConfiguration))
        .then(InvocationContextFilter.parseInvocationContextFromGatewayHeader())
        .then(InvocationContextFilter.addInvocationContextToLoggingStack())

    fun forResponses(): Filter = Filter.NoOp
}