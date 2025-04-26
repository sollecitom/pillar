package sollecitom.libs.pillar.json.serialization.correlation.core.access.origin

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OriginJsonSerializationTests : AcmeJsonSerdeTestSpecification<Origin>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Origin.jsonSerde

    override fun parameterizedArguments() = listOf(
        "with-V4-ip-address" to Origin.create(ipAddress = IpAddress.create("127.0.0.1")),
        "with-V6-ip-address" to Origin.create(ipAddress = IpAddress.create("::1"))
    )
}