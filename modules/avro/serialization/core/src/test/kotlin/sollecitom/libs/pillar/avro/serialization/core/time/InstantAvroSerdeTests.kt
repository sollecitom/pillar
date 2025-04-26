package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import kotlinx.datetime.Instant
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.days

@TestInstance(PER_CLASS)
class InstantAvroSerdeTests : AcmeAvroSerdeTestSpecification<Instant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Instant.avroSerde

    override fun parameterizedArguments() = listOf(
        "now" to clock.now(),
        "2 days ago" to clock.now() - 2.days,
        "9 days from now" to clock.now() + 9.days
    )
}