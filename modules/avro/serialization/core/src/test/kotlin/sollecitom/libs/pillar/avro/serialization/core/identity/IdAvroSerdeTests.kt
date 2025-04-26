package sollecitom.libs.pillar.avro.serialization.core.identity

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class IdAvroSerdeTests : AcmeAvroSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Id.avroSerde

    override fun parameterizedArguments() = listOf(
        "STRING" to StringId(newId.external().stringValue),
        "ULID" to newId.ulid.monotonic(),
        "UUID" to newId.uuid.random(),
        "KSUID" to newId.ksuid.monotonic()
    )
}