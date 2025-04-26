package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSerde
import sollecitom.libs.pillar.avro.serialization.core.time.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

val Event.Reference.Companion.avroSchema get() = EventAvroSchemas.eventReference
val Event.Reference.Companion.avroSerde: AvroSerde<Event.Reference> get() = EventReferenceAvroSerde

private object EventReferenceAvroSerde : AvroSerde<Event.Reference> {

    override val schema get() = Event.Reference.avroSchema

    override fun serialize(value: Event.Reference): GenericRecord = buildRecord {

        setValue(Fields.id, value.id, Id.avroSerde)
        setValue(Fields.type, value.type, Happening.Type.avroSerde)
        setValue(Fields.timestamp, value.timestamp, Instant.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val id = getValue(Fields.id, Id.avroSerde)
        val type = getValue(Fields.type, Happening.Type.avroSerde)
        val timestamp = getValue(Fields.timestamp, Instant.avroSerde)
        Event.Reference(id = id, type = type, timestamp = timestamp)
    }

    private object Fields {
        const val id = "id"
        const val type = "type"
        const val timestamp = "timestamp"
    }
}