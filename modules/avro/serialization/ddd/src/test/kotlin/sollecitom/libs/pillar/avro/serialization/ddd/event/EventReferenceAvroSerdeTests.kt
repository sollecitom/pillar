package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EventReferenceAvroSerdeTests : AcmeAvroSerdeTestSpecification<Event.Reference>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Event.Reference.avroSerde

    override fun parameterizedArguments() = listOf(
        "stubbed" to Event.Reference(type = Happening.Type(name = Name.random(), version = IntVersion(2)), id = newId.external(), timestamp = clock.now()),
    )
}