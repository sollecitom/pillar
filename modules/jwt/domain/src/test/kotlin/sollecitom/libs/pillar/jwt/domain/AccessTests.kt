package sollecitom.libs.pillar.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AccessTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class `realmRoles` {

        @Test
        fun `combines application roles and additional realm roles`() {

            val access = Access(applicationRoles = setOf("customer", "admin"), additionalRealmRoles = setOf("offline_access"), keycloakAccountRoles = setOf("view-profile"))

            assertThat(access.realmRoles).isEqualTo(setOf("customer", "admin", "offline_access"))
        }

        @Test
        fun `with empty application roles`() {

            val access = Access(applicationRoles = emptySet(), additionalRealmRoles = setOf("offline_access"), keycloakAccountRoles = emptySet())

            assertThat(access.realmRoles).isEqualTo(setOf("offline_access"))
        }

        @Test
        fun `with empty additional realm roles`() {

            val access = Access(applicationRoles = setOf("customer"), additionalRealmRoles = emptySet(), keycloakAccountRoles = emptySet())

            assertThat(access.realmRoles).isEqualTo(setOf("customer"))
        }

        @Test
        fun `with all empty roles`() {

            val access = Access(applicationRoles = emptySet(), additionalRealmRoles = emptySet(), keycloakAccountRoles = emptySet())

            assertThat(access.realmRoles).isEqualTo(emptySet())
        }
    }
}
