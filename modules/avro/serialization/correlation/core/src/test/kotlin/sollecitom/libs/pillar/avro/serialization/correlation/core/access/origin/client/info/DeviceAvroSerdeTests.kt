package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.web.client.info.domain.Device
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class DeviceAvroSerdeTests : AcmeAvroSerdeTestSpecification<Device>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Device.avroSerde

    override fun parameterizedArguments() = listOf(
        "fully-populated" to Device.create(),
        "fully-null" to Device.create(className = null, name = null, brand = null),
        "mixed" to Device.create(className = "Laptop", name = null, brand = "Windows"),
    )
}