package sollecitom.libs.pillar.json.serialization.correlation.core.toggles

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.test.utils.toggles.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TogglesJsonSerializationTests : AcmeJsonSerdeTestSpecification<Toggles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Toggles.jsonSerde

    override fun parameterizedArguments() = listOf(
        "empty" to Toggles.create(),
        "not-empty" to notEmptyToggles(),
    )

    private fun notEmptyToggles(): Toggles {

        val boolean = ToggleValue.boolean(value = false)
        val integer = ToggleValue.integer(value = 10)
        val decimal = ToggleValue.decimal(value = -7.83)
        val enum = ToggleValue.enum(Amazing.YES)
        return Toggles(values = setOf(boolean, integer, decimal, enum))
    }

    private enum class Amazing {
        NO, YES
    }
}