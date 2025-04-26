package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class TogglesAvroSerdeTests : AcmeAvroSerdeTestSpecification<Toggles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Toggles.avroSerde

    override fun parameterizedArguments() = listOf(
        "single" to Toggles(values = setOf(BooleanToggleValue(id = newId.external(), value = true))),
        "multiple" to Toggles(values = setOf(BooleanToggleValue(id = newId.external(), value = true), EnumToggleValue(id = newId.external(), value = "A_TOGGLE"), IntegerToggleValue(id = newId.external(), value = 23), DecimalToggleValue(id = newId.external(), value = -71.23))),
    )
}