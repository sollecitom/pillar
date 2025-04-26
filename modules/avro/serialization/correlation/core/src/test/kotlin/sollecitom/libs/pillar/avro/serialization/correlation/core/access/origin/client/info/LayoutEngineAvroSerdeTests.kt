package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.web.client.info.domain.LayoutEngine
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class LayoutEngineAvroSerdeTests : AcmeAvroSerdeTestSpecification<LayoutEngine>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = LayoutEngine.avroSerde

    override fun parameterizedArguments() = listOf(
        "fully-populated" to LayoutEngine.create(),
        "fully-null" to LayoutEngine.create(className = null, name = null, version = null),
        "mixed" to LayoutEngine.create(className = null, name = null, version = "23"),
    )
}