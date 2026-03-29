package sollecitom.libs.pillar.http.api.conventions

import assertk.assertThat
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition

@TestInstance(PER_CLASS)
class AcmeHttpApiDefinitionTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class `companyWide` {

        @Test
        fun `provides a non-null HttpApiDefinition`() {

            val definition = HttpApiDefinition.companyWide

            assertThat(definition).isNotNull()
        }

        @Test
        fun `provides header names`() {

            val definition = HttpApiDefinition.companyWide

            assertThat(definition.headerNames).isNotNull()
        }
    }
}
