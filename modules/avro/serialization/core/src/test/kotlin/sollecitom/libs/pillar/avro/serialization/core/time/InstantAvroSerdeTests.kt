package sollecitom.libs.pillar.avro.serialization.core.time

import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
class InstantAvroSerdeTests : AcmeAvroSerdeTestSpecification<Instant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Instant.avroSerde

    override fun parameterizedArguments() = listOf(
        "now" to clock.now(),
        "2 days ago" to clock.now() - 2.days,
        "9 days from now" to clock.now() + 9.days,
        "nanosecond precision" to Instant.parse("2026-10-06T10:00:00.000000001Z"),
        "before the epoch" to Instant.parse("1969-07-20T20:17:40.123456789Z")
    )
}
