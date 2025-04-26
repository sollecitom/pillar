package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.time.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.ddd.domain.Event
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

val Event.Metadata.Companion.avroSchema get() = EventAvroSchemas.eventMetadata
val Event.Metadata.Companion.avroSerde: AvroSerde<Event.Metadata> get() = EventMetadataAvroSerde

private object EventMetadataAvroSerde : AvroSerde<Event.Metadata> {

    override val schema get() = Event.Metadata.avroSchema

    override fun serialize(value: Event.Metadata): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValue(Fields.timestamp, value.timestamp, Instant.avroSerde)
        setValue(Fields.context, value.context, Event.Context.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val timestamp = getValue(Fields.timestamp, Instant.avroSerde)
        val context = getValue(Fields.context, Event.Context.avroSerde)
        Event.Metadata(id = id, timestamp = timestamp, context = context)
    }

    private object Fields {
        const val id = "id"
        const val timestamp = "timestamp"
        const val context = "context"
    }
}