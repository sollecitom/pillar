package sollecitom.libs.pillar.avro.serialization.ddd.utils

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.time.avroSerde
import sollecitom.libs.pillar.avro.serialization.ddd.event.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroRecordBuilder
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.ddd.domain.Event
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

interface WithEventAvroSerdeSupport {

    context(builder: AvroRecordBuilder)
    fun setEventFields(event: Event) {

        builder.setValue(Fields.id, event.id, Id.avroSerde)
        builder.setValue(Fields.timestamp, event.timestamp, Instant.avroSerde)
        builder.setValue(Fields.context, event.context, Event.Context.avroSerde)
    }

    context(builder: GenericRecord)
    fun getEventFields(): EventFields {

        val id = builder.getValue(Fields.id, Id.avroSerde)
        val timestamp = builder.getValue(Fields.timestamp, Instant.avroSerde)
        val context = builder.getValue(Fields.context, Event.Context.avroSerde)
        return EventFields(id = id, timestamp = timestamp, context = context)
    }

    data class EventFields(val id: Id, val timestamp: Instant, val context: Event.Context)

    object Fields {
        const val id = "id"
        const val timestamp = "timestamp"
        const val context = "context"
    }
}