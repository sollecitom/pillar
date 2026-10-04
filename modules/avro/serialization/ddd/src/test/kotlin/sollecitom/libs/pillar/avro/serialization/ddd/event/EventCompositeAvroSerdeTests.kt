package sollecitom.libs.pillar.avro.serialization.ddd.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.apache.avro.SchemaBuilder
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.ddd.test.utils.create
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class EventCompositeAvroSerdeTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val testDataSchema: Schema = SchemaBuilder.record("TestData").namespace("acme.test").fields().requiredString("text").endRecord()
    private val testCompositeSchema: Schema = SchemaBuilder.record("TestComposite").namespace("acme.test").fields().name("data").type(testDataSchema).noDefault().name("metadata").type(Event.Metadata.avroSchema).noDefault().endRecord()

    @Test
    fun `serializing and deserializing preserves both the data and the metadata`() {

        val serde = Event.Composite.avroSerde(testCompositeSchema, ::serializeTestData, ::deserializeTestData)
        val data = TestData(text = "some text")
        val metadata = Event.Metadata(id = newId.external(), timestamp = Instant.fromEpochMilliseconds(1700000000000), context = Event.Context.create())
        val event = Event.Composite(data = data, metadata = metadata)

        val deserialized = serde.deserialize(serde.serialize(event))

        assertThat(deserialized.data).isEqualTo(TestData(text = "some text"))
        assertThat(deserialized.metadata).isEqualTo(metadata)
    }

    private fun serializeTestData(data: TestData): GenericRecord = GenericData.Record(testDataSchema).apply { put("text", data.text) }

    private fun deserializeTestData(record: GenericRecord) = TestData(text = record.get("text").toString())

    private data class TestData(val text: String) : Event.Data {

        override val type: Happening.Type get() = TYPE

        companion object {
            val TYPE = Happening.Type(name = Name("test-data"), version = IntVersion(value = 1))
        }
    }
}
