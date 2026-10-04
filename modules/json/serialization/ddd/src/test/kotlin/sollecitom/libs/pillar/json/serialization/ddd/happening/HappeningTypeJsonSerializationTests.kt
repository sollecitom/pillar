package sollecitom.libs.pillar.json.serialization.ddd.happening

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.ddd.domain.Happening

@TestInstance(PER_CLASS)
class HappeningTypeJsonSerializationTests : AcmeJsonSerdeTestSpecification<Happening.Type> {

    override val jsonSerde get() = Happening.Type.jsonSerde

    override fun parameterizedArguments() = listOf(
        "stubbed" to Happening.Type(name = Name("account-created"), version = IntVersion(value = 1))
    )
}
