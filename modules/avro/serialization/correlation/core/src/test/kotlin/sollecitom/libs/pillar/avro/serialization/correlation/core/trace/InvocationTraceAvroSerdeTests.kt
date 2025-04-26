package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class InvocationTraceAvroSerdeTests : AcmeAvroSerdeTestSpecification<InvocationTrace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = InvocationTrace.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomized" to InvocationTrace(id = newId.ulid.monotonic(), createdAt = clock.now()),
    )
}