package sollecitom.libs.pillar.avro.serialization.core.identity

import assertk.assertThat
import assertk.assertions.hasMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.pillar.avro.serialization.test.utils.serializeWithUnknownEnumSymbol
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
class IdAvroSerdeTests : AcmeAvroSerdeTestSpecification<Id>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Id.avroSerde

    override fun parameterizedArguments() = listOf(
        "STRING" to StringId(newId.external().stringValue),
        "ULID" to newId.ulid.monotonic(),
        "UUID" to newId.uuid.random(),
        "UUIDV7" to newId.uuid.v7(),
        "KSUID" to newId.ksuid.monotonic()
    )

    @Test
    fun `an unknown Id type fails with a clear error`() {

        val record = avroSerde.serializeWithUnknownEnumSymbol(newId.ulid.monotonic(), fieldName = "type")
        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().hasMessage("Field 'type' is 'UNKNOWN': the writer used a symbol this reader doesn't know")
    }
}
