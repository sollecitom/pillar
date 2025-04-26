package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.IntegerToggleValue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class IntegerToggleValueAvroSerdeTests : AcmeAvroSerdeTestSpecification<IntegerToggleValue>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = IntegerToggleValue.avroSerde

    override fun parameterizedArguments() = listOf(
        "min_long" to IntegerToggleValue(id = newId.external(), value = Long.MIN_VALUE),
        "max_long" to IntegerToggleValue(id = newId.external(), value = Long.MAX_VALUE),
        "zero" to IntegerToggleValue(id = newId.external(), value = 0L),
        "positive" to IntegerToggleValue(id = newId.external(), value = 23L),
        "negative" to IntegerToggleValue(id = newId.external(), value = -192323L)
    )
}