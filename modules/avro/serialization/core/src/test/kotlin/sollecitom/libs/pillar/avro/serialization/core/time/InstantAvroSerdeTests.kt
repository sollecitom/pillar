package sollecitom.libs.pillar.avro.serialization.core.time

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.isInstanceOf
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.pillar.avro.serialization.test.utils.serializeWithUnknownEnumSymbol
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
class InstantAvroSerdeTests : AcmeAvroSerdeTestSpecification<Instant>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Instant.avroSerde

    override fun parameterizedArguments() = listOf(
        "now" to clock.now(),
        "2 days ago" to clock.now() - 2.days,
        "9 days from now" to clock.now() + 9.days
    )

    @Test
    fun `an unknown timestamp format fails with a clear error`() {

        val record = avroSerde.serializeWithUnknownEnumSymbol(clock.now(), fieldName = "format")

        assertFailure { avroSerde.deserialize(record) }.isInstanceOf<IllegalStateException>().hasMessage("Field 'format' is 'UNKNOWN': the writer used a symbol this reader doesn't know")
    }
}
