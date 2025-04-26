package sollecitom.libs.pillar.json.schema.rules

import sollecitom.libs.pillar.json.schema.rules.specs.AdditionalPropertiesTestSpecification
import sollecitom.libs.pillar.json.schema.rules.specs.DisallowedConstKeywordTestSpecification
import sollecitom.libs.pillar.json.schema.rules.specs.PropertyNamesTestSpecification
import sollecitom.libs.pillar.json.schema.rules.specs.RequiredPropertiesTestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeJsonSchemaRulesTests {

    @Nested
    inner class PropertyNames : PropertyNamesTestSpecification {

        override val rules get() = AcmeJsonSchemaRules
    }

    @Nested
    inner class AdditionalProperties : AdditionalPropertiesTestSpecification {

        override val rules get() = AcmeJsonSchemaRules
    }

    @Nested
    inner class RequiredProperties : RequiredPropertiesTestSpecification {

        override val rules get() = AcmeJsonSchemaRules
    }

    @Nested
    inner class DisallowedConstKeyword : DisallowedConstKeywordTestSpecification {

        override val rules get() = AcmeJsonSchemaRules
    }
}
