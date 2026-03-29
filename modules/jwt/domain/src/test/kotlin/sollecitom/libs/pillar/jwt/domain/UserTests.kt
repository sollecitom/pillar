package sollecitom.libs.pillar.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class UserTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class `fullName` {

        @Test
        fun `with no other names`() {

            val user = User(id = "user-1", organization = Organization(id = "org-1", name = "acme"), userName = "jdoe", emailAddress = "john@acme.com", firstName = "John", lastName = "Doe", otherNames = emptyList())

            assertThat(user.fullName).isEqualTo("John Doe")
        }

        @Test
        fun `with one other name`() {

            val user = User(id = "user-1", organization = Organization(id = "org-1", name = "acme"), userName = "jdoe", emailAddress = "john@acme.com", firstName = "John", lastName = "Doe", otherNames = listOf("William"))

            assertThat(user.fullName).isEqualTo("JohnWilliam Doe")
        }

        @Test
        fun `with multiple other names`() {

            val user = User(id = "user-1", organization = Organization(id = "org-1", name = "acme"), userName = "jdoe", emailAddress = "john@acme.com", firstName = "John", lastName = "Doe", otherNames = listOf("William", "James"))

            assertThat(user.fullName).isEqualTo("JohnWilliam James Doe")
        }
    }
}
