package sollecitom.libs.pillar.prometheus.micrometer

import assertk.assertThat
import assertk.assertions.isTrue
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.acme.conventions.MonitoringConventions

@TestInstance(PER_CLASS)
class PrometheusMicrometerRegistryConventionsTests : MonitoringConventions {

    @Test
    fun `closing the registry closes the binders that hold resources`() {

        val binder = CloseableBinder()
        val registry = prometheusMeterRegistry(meterBinders = listOf(binder))

        registry.close()

        assertThat(binder.closed).isTrue()
    }

    private class CloseableBinder : MeterBinder, AutoCloseable {

        var closed = false

        override fun bindTo(registry: MeterRegistry) {}

        override fun close() {
            closed = true
        }
    }
}
