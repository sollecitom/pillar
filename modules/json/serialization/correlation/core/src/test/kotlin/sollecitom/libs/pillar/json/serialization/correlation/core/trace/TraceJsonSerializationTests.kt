package sollecitom.libs.pillar.json.serialization.correlation.core.trace

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.correlation.core.test.utils.trace.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TraceJsonSerializationTests : AcmeJsonSerdeTestSpecification<Trace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Trace.jsonSerde

    override fun parameterizedArguments() = listOf(
        "standard" to Trace.create()
    )
}