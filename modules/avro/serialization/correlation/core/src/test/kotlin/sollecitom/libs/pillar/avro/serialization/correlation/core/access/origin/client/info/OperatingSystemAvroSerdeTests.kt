package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.web.client.info.domain.OperatingSystem
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OperatingSystemAvroSerdeTests : AcmeAvroSerdeTestSpecification<OperatingSystem>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = OperatingSystem.avroSerde

    override fun parameterizedArguments() = listOf(
        "fully-populated" to OperatingSystem.create(),
        "fully-null" to OperatingSystem.create(className = null, name = null, version = null),
        "mixed" to OperatingSystem.create(className = null, name = "Windows", version = "XP"),
    )
}