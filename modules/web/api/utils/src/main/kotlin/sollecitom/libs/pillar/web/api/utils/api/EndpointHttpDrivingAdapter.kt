package sollecitom.libs.pillar.web.api.utils.api

import io.micrometer.core.instrument.MeterRegistry
import org.http4k.config.Environment
import org.http4k.core.Request
import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.HttpDrivingAdapter

class EndpointHttpDrivingAdapter internal constructor(private val endpoints: Set<Endpoint>, private val configuration: HttpDrivingAdapter.Configuration, private val meterRegistry: MeterRegistry, private val coreDataGenerator: CoreDataGenerator, httpApiDefinition: HttpApiDefinition) : HttpDrivingAdapter, CoreDataGenerator by coreDataGenerator, HttpApiDefinition by httpApiDefinition {

    internal constructor(endpoints: Set<Endpoint>, environment: Environment, meterRegistry: MeterRegistry, coreDataGenerator: CoreDataGenerator, httpApiDefinition: HttpApiDefinition) : this(endpoints, HttpDrivingAdapter.Configuration.from(environment), meterRegistry, coreDataGenerator, httpApiDefinition)

    private val api = mainHttpApi()
    override val port: Port get() = api.port

    override fun invoke(request: Request) = api(request)

    override suspend fun start() {

        api.start()
        logger.info { "Started on port ${api.port.value}" }
    }

    override suspend fun stop() {

        api.stop()
        logger.info { "Stopped" }
    }

    private fun mainHttpApi() = mainHttpApi(endpoints = endpoints, requestedPort = configuration.requestedPort, meterRegistry = meterRegistry)

    companion object : Loggable()
}

context(generator: CoreDataGenerator, api: HttpApiDefinition)
fun EndpointHttpDrivingAdapter.Companion.create(endpoints: Set<Endpoint>, environment: Environment, meterRegistry: MeterRegistry) = EndpointHttpDrivingAdapter(endpoints, environment, meterRegistry, generator, api)

context(generator: CoreDataGenerator, api: HttpApiDefinition)
fun EndpointHttpDrivingAdapter.Companion.create(endpoints: Set<Endpoint>, configuration: HttpDrivingAdapter.Configuration, meterRegistry: MeterRegistry) = EndpointHttpDrivingAdapter(endpoints, configuration, meterRegistry, generator, api)
