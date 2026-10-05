package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.avro.generic.GenericData
import assertk.assertions.messageContains
import assertk.assertThat
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import kotlin.time.Instant
import org.junit.jupiter.api.Test
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

    @Test
    fun `a timestamp format this reader does not know fails with a clear error`() {

        val record = GenericRecordBuilder(avroSerde.schema).set("format", unknownSymbolOf("format")).set("value", "2026-10-06T00:00:00Z").build()

        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().messageContains("the writer used a format this reader doesn't know")
    }

    private fun unknownSymbolOf(fieldName: String) = GenericData.EnumSymbol(avroSerde.schema.getField(fieldName).schema(), "UNKNOWN")
}
