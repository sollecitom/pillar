package sollecitom.libs.pillar.avro.schema.rules

import sollecitom.libs.swissknife.avro.schema.checker.*
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRuleSet
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.upperCaseLetters
import org.apache.avro.Schema

object AcmeAvroSchemaRules : ComplianceRuleSet<Schema> {

    private const val MANDATORY_NAMESPACE_PREFIX = "acme."
    private val whitelistedSchemaNameAlphabet by lazy { (lowercaseCaseLetters + upperCaseLetters + '_').toSet() }
    private val whitelistedNamespaceAlphabet by lazy { (lowercaseCaseLetters + '.' + '_').toSet() }
    private val whitelistedFieldNameAlphabet by lazy { (lowercaseCaseLetters + '_').toSet() }

    override val rules by lazy {
        setOf(
            WhitelistedAlphabetSchemaNameRule(alphabet = whitelistedSchemaNameAlphabet),
            UppercaseSchemaNameRule,
            MandatoryNamespacePrefixRule(prefix = MANDATORY_NAMESPACE_PREFIX),
            WhitelistedAlphabetNamespaceNameRule(alphabet = whitelistedNamespaceAlphabet),
            WhitelistedAlphabetFieldNameRule(alphabet = whitelistedFieldNameAlphabet)
        )
    }
}