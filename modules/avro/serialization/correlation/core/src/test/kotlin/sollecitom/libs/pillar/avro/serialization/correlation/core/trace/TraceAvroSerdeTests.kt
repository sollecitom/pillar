package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import sollecitom.libs.swissknife.correlation.core.test.utils.trace.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TraceAvroSerdeTests : AcmeAvroSerdeTestSpecification<Trace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Trace.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomized" to Trace.create(),
    )
}