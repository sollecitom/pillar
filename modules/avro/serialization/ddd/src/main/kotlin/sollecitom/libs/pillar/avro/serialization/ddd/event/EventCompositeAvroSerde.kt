package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.ddd.domain.Event
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

fun <DATA : Event.Data> Event.Composite.Companion.avroSerde(schema: Schema, serializeData: (DATA) -> GenericRecord, deserializeData: (GenericRecord) -> DATA): AvroSerde<Event.Composite<DATA>> = EventCompositeAvroSerde(schema, serializeData, deserializeData)

fun <DATA : Event.Data> Event.Composite.Companion.avroSerde(schema: Schema, dataSerde: AvroSerde<DATA>): AvroSerde<Event.Composite<DATA>> = avroSerde(schema, dataSerde::serialize, dataSerde::deserialize)

private class EventCompositeAvroSerde<DATA : Event.Data>(override val schema: Schema, private val serializeData: (DATA) -> GenericRecord, private val deserializeData: (GenericRecord) -> DATA) : AvroSerde<Event.Composite<DATA>> {

    override fun serialize(value: Event.Composite<DATA>): GenericRecord = buildRecord {

        set(Fields.data, value.data.let(serializeData))
        setValue(Fields.metadata, value.metadata, Event.Metadata.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val data = getRecord(Fields.data).let(deserializeData)
        val metadata = getValue(Fields.metadata, Event.Metadata.avroSerde)
        Event.Composite(data = data, metadata = metadata)
    }

    private object Fields {
        const val data = "data"
        const val metadata = "metadata"
    }
}