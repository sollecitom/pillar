package sollecitom.libs.pillar.openapi.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.letters
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiField
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiFields
import sollecitom.libs.swissknife.openapi.checking.checker.rules.*
import sollecitom.libs.swissknife.openapi.checking.checker.rules.field.MandatorySuffixTextFieldRule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem.HttpMethod.*

object AcmeOpenApiRules : ComplianceRuleSet<OpenAPI> {

    private val mediaTypesForRequestsThatMustHaveAnExample = setOf("application/json")
    private val mediaTypesForResponsesThatMustHaveAnExample = setOf("application/json", "text/plain")
    private val mediaTypesThatShouldComplyWithTheirSchema = setOf("application/json")
    private val textFieldMustEndWithFullStop = MandatorySuffixTextFieldRule(".", true)
    private val whitelistedPathAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedPathParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedQueryParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedCookiesAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedHeadersAlphabet by lazy { (letters + '-').toSet() }
    private val requiredOperationFields by lazy { setOf(OpenApiField("operationId", Operation::getOperationId), OpenApiField("summary", Operation::getSummary)) }
    private val whitelistedOpenApiVersions by lazy { setOf("3.1.0", "3.0.0") }
    private val versioningPathSegmentRegex = Regex("v[1-9]+\$")

    private val mandatoryRequestBodyRule by lazy { MandatoryRequestBodyRule(methods = setOf(POST to true, PUT to true, PATCH to true)) }
    private val forbiddenRequestBodyRule by lazy { ForbiddenRequestBodyRule(methods = setOf(GET, DELETE, HEAD, TRACE, OPTIONS)) }
    private val pathNameRule by lazy { WhitelistedAlphabetPathNameRule(alphabet = whitelistedPathAlphabet, allowedPathSegments = setOf(versioningPathSegmentRegex)) }
    private val parametersNameRule by lazy { WhitelistedAlphabetParameterNameRule(pathAlphabet = whitelistedPathParametersAlphabet, headerAlphabet = whitelistedHeadersAlphabet, queryAlphabet = whitelistedQueryParametersAlphabet, cookieAlphabet = whitelistedCookiesAlphabet) }
    private val mandatoryRequestBodyContentMediaTypeRule by lazy { MandatoryRequestBodyContentMediaTypesRule(methodsToCheck = setOf(POST, PUT, PATCH)) }
    private val mandatoryRequestBodyDescriptionRule by lazy { MandatoryRequestBodyDescriptionRule(methods = setOf(POST, PUT, PATCH)) }
    private val mandatoryRequestBodyExampleRule by lazy { MandatoryRequestBodyExampleRule(methods = setOf(POST, PUT, PATCH), mediaTypesThatShouldHaveAnExample = mediaTypesForRequestsThatMustHaveAnExample) }
    private val mandatoryResponseBodyExampleRule by lazy { MandatoryResponseBodyExampleRule(methods = setOf(POST, PUT, PATCH), mediaTypesThatShouldHaveAnExample = mediaTypesForResponsesThatMustHaveAnExample) }
    private val exampleSchemaComplianceRule by lazy { ExamplesSchemaComplianceRule(mediaTypesToCheck = mediaTypesThatShouldComplyWithTheirSchema) }
    private val whitelistedOpenApiVersionFieldRule by lazy { WhitelistedOpenApiVersionFieldRule(whitelistedOpenApiVersions = whitelistedOpenApiVersions) }
    private val mandatoryInfoFieldsRule by lazy { MandatoryInfoFieldsRule(requiredFields = setOf(OpenApiFields.Info.title, OpenApiFields.Info.description)) }

    private val operationTextFieldRules by lazy {
        FieldSpecificRules(
            OpenApiFields.Operation.summary to setOf(textFieldMustEndWithFullStop),
            OpenApiFields.Operation.description to setOf(textFieldMustEndWithFullStop),
            OpenApiFields.Operation.RequestBody.description to setOf(textFieldMustEndWithFullStop)
        )
    }

    override val rules: Set<ComplianceRule<OpenAPI>> by lazy {
        setOf(
            pathNameRule,
            parametersNameRule,
            DisallowReservedCharactersInParameterNameRule,
            MandatoryOperationFieldsRule(requiredFields = requiredOperationFields),
            EnforceOperationDescriptionDifferentFromSummaryRule,
            EnforceCamelCaseOperationIdRule,
            mandatoryRequestBodyRule,
            mandatoryRequestBodyContentMediaTypeRule,
            forbiddenRequestBodyRule,
            mandatoryRequestBodyDescriptionRule,
            operationTextFieldRules,
            whitelistedOpenApiVersionFieldRule,
            mandatoryInfoFieldsRule,
            mandatoryRequestBodyExampleRule,
            mandatoryResponseBodyExampleRule,
            exampleSchemaComplianceRule
        ) + AcmeTracingHeadersOpenApiRules.rules
    }
}