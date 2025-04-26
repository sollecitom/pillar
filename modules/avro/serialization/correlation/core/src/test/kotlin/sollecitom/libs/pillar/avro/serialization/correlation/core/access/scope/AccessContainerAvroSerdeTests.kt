package sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessContainerAvroSerdeTests : AcmeAvroSerdeTestSpecification<AccessContainer>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = AccessContainer.avroSerde

    override fun parameterizedArguments() = listOf(
        "randomised" to AccessContainer(id = newId.external())
    )
}