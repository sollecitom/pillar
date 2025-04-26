package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class EnumToggleValueAvroSerdeTests : AcmeAvroSerdeTestSpecification<EnumToggleValue>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = EnumToggleValue.avroSerde

    override fun parameterizedArguments() = listOf(
        "some_value" to EnumToggleValue(id = newId.external(), value = "SOME_VALUE"),
    )
}