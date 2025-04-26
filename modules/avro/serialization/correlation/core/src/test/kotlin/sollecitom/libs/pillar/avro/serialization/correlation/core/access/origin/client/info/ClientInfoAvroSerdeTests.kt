package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.test.utils.access.origin.create
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ClientInfoAvroSerdeTests : AcmeAvroSerdeTestSpecification<ClientInfo>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = ClientInfo.avroSerde

    override fun parameterizedArguments() = listOf(
        "stubbed" to ClientInfo.create(),
    )
}