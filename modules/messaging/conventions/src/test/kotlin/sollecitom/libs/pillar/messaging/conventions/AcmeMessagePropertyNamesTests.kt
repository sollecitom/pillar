package sollecitom.libs.pillar.messaging.conventions

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeMessagePropertyNamesTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class `forEvents` {

        @Test
        fun `type property name has expected value`() {

            assertThat(AcmeMessagePropertyNames.forEvents.type).isEqualTo("event-type")
        }
    }
}
