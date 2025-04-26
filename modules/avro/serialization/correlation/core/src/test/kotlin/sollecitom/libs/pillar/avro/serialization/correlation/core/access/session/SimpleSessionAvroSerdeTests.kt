package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class SimpleSessionAvroSerdeTests : AcmeAvroSerdeTestSpecification<SimpleSession>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = SimpleSession.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomised" to SimpleSession(id = newId.external())
    )
}