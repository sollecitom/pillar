package sollecitom.libs.pillar.json.serialization.core.unit

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification

@TestInstance(PER_CLASS)
class UnitJsonSerializationTests : AcmeJsonSerdeTestSpecification<Unit> {

    override val jsonSerde get() = Unit.jsonSerde

    override fun parameterizedArguments() = listOf(
        "unit" to Unit
    )
}
