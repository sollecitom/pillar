package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.correlation.core.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import org.apache.avro.generic.GenericRecord

/** Avro schema for [Event.Context]. */
val Event.Context.Companion.avroSchema get() = EventAvroSchemas.eventContext
/** Avro serializer/deserializer for [Event.Context], including invocation context and optional parent/originating references. */
val Event.Context.Companion.avroSerde: AvroSerde<Event.Context> get() = EventContextAvroSerde

private object EventContextAvroSerde : AvroSerde<Event.Context> {

    override val schema get() = Event.Context.avroSchema

    override fun serialize(value: Event.Context): GenericRecord = buildRecord {

        setValue(Fields.invocation, value.invocation, InvocationContext.avroSerde)
        setValueOrNull(Fields.parent, value.parent, Event.Reference.avroSerde)
        setValueOrNull(Fields.originating, value.originating, Event.Reference.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val invocation = getValue(Fields.invocation, InvocationContext.avroSerde)
        val parent = getValueOrNull(Fields.parent, Event.Reference.avroSerde)
        val originating = getValueOrNull(Fields.originating, Event.Reference.avroSerde)
        Event.Context(invocation = invocation, parent = parent, originating = originating)
    }

    private object Fields {
        const val invocation = "invocation"
        const val parent = "parent"
        const val originating = "originating"
    }
}