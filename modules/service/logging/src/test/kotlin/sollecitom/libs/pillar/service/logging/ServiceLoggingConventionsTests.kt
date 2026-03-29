package sollecitom.libs.pillar.service.logging

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.acme.conventions.LoggingConventions

@TestInstance(PER_CLASS)
class ServiceLoggingConventionsTests {

    private val conventions = object : LoggingConventions {}

    @Nested
    @TestInstance(PER_CLASS)
    inner class `serviceStartedLogMessage` {

        @Test
        fun `returns expected message`() {

            val message = with(conventions) { serviceStartedLogMessage }

            assertThat(message).isEqualTo("--Service started--")
        }

        @Test
        fun `returns non-empty message`() {

            val message = with(conventions) { serviceStartedLogMessage }

            assertThat(message).isNotEmpty()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class `serviceStoppedLogMessage` {

        @Test
        fun `returns expected message`() {

            val message = with(conventions) { serviceStoppedLogMessage }

            assertThat(message).isEqualTo("--Service stopped--")
        }

        @Test
        fun `returns non-empty message`() {

            val message = with(conventions) { serviceStoppedLogMessage }

            assertThat(message).isNotEmpty()
        }
    }
}
