package sollecitom.libs.pillar.openapi.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import io.swagger.v3.oas.models.OpenAPI

internal object AcmeTracingHeadersOpenApiRules : ComplianceRuleSet<OpenAPI> {

    override val rules: Set<ComplianceRule<OpenAPI>> by lazy { // put some rules here
        setOf(

        )
    }
}