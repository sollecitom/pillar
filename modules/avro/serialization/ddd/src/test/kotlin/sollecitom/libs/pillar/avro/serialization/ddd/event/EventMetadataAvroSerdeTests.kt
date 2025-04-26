package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.test.utils.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EventMetadataAvroSerdeTests : AcmeAvroSerdeTestSpecification<Event.Metadata>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Event.Metadata.avroSerde

    override fun parameterizedArguments() = listOf(
        "random" to Event.Metadata(id = newId.external(), timestamp = clock.now(), context = Event.Context.create()),
    )
}