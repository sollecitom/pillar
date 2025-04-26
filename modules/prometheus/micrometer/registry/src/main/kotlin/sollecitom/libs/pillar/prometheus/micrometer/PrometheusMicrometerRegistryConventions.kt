package sollecitom.libs.pillar.prometheus.micrometer

import sollecitom.libs.pillar.acme.conventions.MonitoringConventions
import io.micrometer.core.instrument.binder.MeterBinder
import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.micrometer.prometheusmetrics.PrometheusRenameFilter
import java.io.File

context(_: MonitoringConventions)
fun prometheusMeterRegistry(meterBinders: List<MeterBinder> = standardMicrometerMeterBinders()): PrometheusMeterRegistry {

    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    registry.throwExceptionOnRegistrationFailure().config().meterFilter(PrometheusRenameFilter())
    meterBinders.forEach { it.bindTo(registry) }
    return registry
}

context(_: MonitoringConventions)
fun standardMicrometerMeterBinders(): List<MeterBinder> = listOf(
    JvmInfoMetrics(),
    JvmMemoryMetrics(),
    JvmGcMetrics(),
    JvmHeapPressureMetrics(),
    JvmCompilationMetrics(),
    JvmThreadMetrics(),
    ClassLoaderMetrics(),
    DiskSpaceMetrics(File("/tmp/.")),
    FileDescriptorMetrics(),
    ProcessorMetrics(),
    UptimeMetrics(),
)
