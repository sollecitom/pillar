package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.BooleanToggleValue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class BooleanToggleValueAvroSerdeTests : AcmeAvroSerdeTestSpecification<BooleanToggleValue>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = BooleanToggleValue.avroSerde

    override fun parameterizedArguments() = listOf(
        "true" to BooleanToggleValue(id = newId.external(), value = true),
        "false" to BooleanToggleValue(id = newId.external(), value = false)
    )
}