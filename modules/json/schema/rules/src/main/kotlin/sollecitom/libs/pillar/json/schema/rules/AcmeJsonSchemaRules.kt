package sollecitom.libs.pillar.json.schema.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.checker.rules.*
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters

object AcmeJsonSchemaRules : ComplianceRuleSet<JsonSchema> {

    private val whitelistedFieldNameAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private const val enforcedAdditionalPropertiesValue = false
    private const val affectPureUnionTypesInAdditionalPropertiesRules = false

    override val rules by lazy {
        setOf(
            WhitelistedAlphabetFieldNameRule(alphabet = whitelistedFieldNameAlphabet),
            MandatoryAdditionalPropertiesRule(affectPureUnionTypes = affectPureUnionTypesInAdditionalPropertiesRules),
            EnforcedAdditionalPropertiesValueRule(enforcedValue = enforcedAdditionalPropertiesValue, affectPureUnionTypes = affectPureUnionTypesInAdditionalPropertiesRules),
            DisallowRequiringUndeclaredPropertiesRule,
            DisallowConstKeywordRule
        )
    }
}