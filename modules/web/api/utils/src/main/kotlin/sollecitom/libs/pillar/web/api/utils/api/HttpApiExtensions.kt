package sollecitom.libs.pillar.web.api.utils.api

import io.micrometer.core.instrument.MeterRegistry
import sollecitom.libs.pillar.web.api.utils.filters.StandardHttpFilter
import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.api.mainHttpApi

context(_: HttpApiDefinition, _: CoreDataGenerator)
fun mainHttpApi(endpoints: Set<Endpoint>, requestedPort: RequestedPort, meterRegistry: MeterRegistry) = mainHttpApi(endpoints, requestedPort, StandardHttpFilter.forRequests(meterRegistry), StandardHttpFilter.forResponses())