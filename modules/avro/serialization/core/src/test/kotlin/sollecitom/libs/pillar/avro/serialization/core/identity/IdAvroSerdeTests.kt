package sollecitom.libs.pillar.avro.serialization.core.identity

import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.avro.generic.GenericData
import assertk.assertions.messageContains
import assertk.assertThat
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

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
    fun `an Id type this reader does not know fails with a clear error`() {

        val record = GenericRecordBuilder(avroSerde.schema).set("type", unknownSymbolOf("type")).set("value", "1").build()

        val result = runCatching { avroSerde.deserialize(record) }

        assertThat(result).failedThrowing<IllegalStateException>().messageContains("the writer used an Id type this reader doesn't know")
    }

    private fun unknownSymbolOf(fieldName: String) = GenericData.EnumSymbol(avroSerde.schema.getField(fieldName).schema(), "UNKNOWN")
}
