package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ExternalInvocationTraceAvroSerdeTests : AcmeAvroSerdeTestSpecification<ExternalInvocationTrace>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = ExternalInvocationTrace.avroSerde

    override fun parameterizedArguments() = listOf(
        "ULID" to ExternalInvocationTrace(invocationId = newId.ulid.monotonic(), actionId = newId.ulid.monotonic()),
        "KSUID" to ExternalInvocationTrace(invocationId = newId.ksuid.monotonic(), actionId = newId.ksuid.monotonic()),
        "UUID" to ExternalInvocationTrace(invocationId = newId.uuid.random(), actionId = newId.uuid.random()),
        "StringId" to ExternalInvocationTrace(invocationId = StringId("invocation_id_1"), actionId = StringId("action_id_1")),
        "mixed" to ExternalInvocationTrace(invocationId = newId.ulid.monotonic(), actionId = StringId("action_id_2")),
    )
}