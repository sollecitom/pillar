package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OriginAvroSerdeTests : AcmeAvroSerdeTestSpecification<Origin>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Origin.avroSerde

    override fun parameterizedArguments() = listOf(
        "V4_ipAddress" to Origin.create(ipAddress = IpAddress.V4.localhost),
        "V6_ipAddress" to Origin.create(ipAddress = IpAddress.V6.localhost)
    )
}