package sollecitom.libs.pillar.http.api.conventions

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        fun `provides the Acme correlation header names`() {

            val headerNames = HttpApiDefinition.companyWide.headerNames

            assertThat(headerNames.correlation.invocationContext).isEqualTo("x-acme-invocation-context")
        }

        @Test
        fun `provides the Acme gateway header names`() {

            val headerNames = HttpApiDefinition.companyWide.headerNames

            assertThat(headerNames.gateway.externalInvocationId).isEqualTo("x-acme-external-trace-invocation-id")
            assertThat(headerNames.gateway.externalActionId).isEqualTo("x-acme-external-trace-action-id")
            assertThat(headerNames.gateway.specifiedLocale).isEqualTo("x-acme-specified-locale-language-tag")
            assertThat(headerNames.gateway.specifiedTargetTenant).isEqualTo("x-acme-specified-target-tenant")
            assertThat(headerNames.gateway.specifiedTargetCustomerId).isEqualTo("x-acme-specified-target-customer-id")
            assertThat(headerNames.gateway.isTest).isEqualTo("x-acme-is-test")
            assertThat(headerNames.gateway.toggles).isEqualTo("x-acme-toggles")
        }
    }
}
