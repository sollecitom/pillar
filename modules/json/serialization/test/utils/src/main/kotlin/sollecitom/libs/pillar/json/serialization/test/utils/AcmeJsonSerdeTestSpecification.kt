package sollecitom.libs.pillar.json.serialization.test.utils

import sollecitom.libs.pillar.json.schema.rules.AcmeJsonSchemaRules
import sollecitom.libs.swissknife.json.test.utils.JsonSerdeTestSpecification
import sollecitom.libs.swissknife.json.test.utils.checker.JsonSchemaComplianceTestSpecification
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface AcmeJsonSerdeTestSpecification<VALUE : Any> : JsonSerdeTestSpecification<VALUE>, JsonSchemaComplianceTestSpecification {

    override val jsonSchema get() = jsonSerde.schema
    override val complianceRules get() = AcmeJsonSchemaRules
}