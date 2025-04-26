package sollecitom.libs.pillar.avro.serialization.correlation.core.toggles

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.DecimalToggleValue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class DecimalToggleValueAvroSerdeTests : AcmeAvroSerdeTestSpecification<DecimalToggleValue>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = DecimalToggleValue.avroSerde

    override fun parameterizedArguments() = listOf(
        "min_double" to DecimalToggleValue(id = newId.external(), value = Double.MIN_VALUE),
        "max_double" to DecimalToggleValue(id = newId.external(), value = Double.MAX_VALUE),
        "zero" to DecimalToggleValue(id = newId.external(), value = 0.0),
        "positive" to DecimalToggleValue(id = newId.external(), value = 2203193.1223),
        "negative" to DecimalToggleValue(id = newId.external(), value = -1294521.17)
    )
}