package sollecitom.libs.pillar.acme.business.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeRolesTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class `contains` {

        @Test
        fun `returns true for customer role`() {

            assertThat(AcmeRoles.customer in AcmeRoles).isTrue()
        }

        @Test
        fun `returns true for expert role`() {

            assertThat(AcmeRoles.expert in AcmeRoles).isTrue()
        }

        @Test
        fun `returns true for admin role`() {

            assertThat(AcmeRoles.admin in AcmeRoles).isTrue()
        }

        @Test
        fun `returns false for unknown role`() {

            assertThat("unknown-role" in AcmeRoles).isFalse()
        }

        @Test
        fun `returns false for empty string`() {

            assertThat("" in AcmeRoles).isFalse()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class `values` {

        @Test
        fun `contains all defined roles`() {

            assertThat(AcmeRoles.values).isEqualTo(setOf("customer", "expert", "admin"))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class `role constants` {

        @Test
        fun `customer role has expected value`() {

            assertThat(AcmeRoles.customer).isEqualTo("customer")
        }

        @Test
        fun `expert role has expected value`() {

            assertThat(AcmeRoles.expert).isEqualTo("expert")
        }

        @Test
        fun `admin role has expected value`() {

            assertThat(AcmeRoles.admin).isEqualTo("admin")
        }
    }
}
