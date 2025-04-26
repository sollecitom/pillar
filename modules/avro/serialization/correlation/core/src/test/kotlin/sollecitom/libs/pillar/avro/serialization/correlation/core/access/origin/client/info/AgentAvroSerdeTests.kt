package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.web.client.info.domain.Agent
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AgentAvroSerdeTests : AcmeAvroSerdeTestSpecification<Agent>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Agent.avroSerde

    override fun parameterizedArguments() = listOf(
        "fully-populated" to Agent.create(),
        "fully-null" to Agent.create(className = null, name = null, version = null),
        "mixed" to Agent.create(className = "Laptop", name = null, version = "1.0.2"),
    )
}