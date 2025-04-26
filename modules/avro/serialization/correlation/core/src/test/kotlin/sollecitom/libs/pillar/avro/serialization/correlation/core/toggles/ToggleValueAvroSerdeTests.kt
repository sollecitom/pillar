package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ToggleValueAvroSerdeTests : AcmeAvroSerdeTestSpecification<ToggleValue<*>>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = ToggleValue.avroSerde

    override fun parameterizedArguments() = listOf(
        "boolean" to BooleanToggleValue(id = newId.external(), value = true),
        "integer" to IntegerToggleValue(id = newId.external(), value = 12),
        "decimal" to DecimalToggleValue(id = newId.external(), value = -23.82),
        "enum" to EnumToggleValue(id = newId.external(), value = "A_VALUE_2"),
    )
}