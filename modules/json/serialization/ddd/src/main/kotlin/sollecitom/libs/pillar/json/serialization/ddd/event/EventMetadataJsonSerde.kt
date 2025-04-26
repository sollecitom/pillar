package sollecitom.libs.pillar.json.serialization.ddd.event

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.json.utils.getRequiredInstant
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.putInstant
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object EventMetadataJsonSerde : JsonSerde.SchemaAware<Event.Metadata> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/event/EventMetadata.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Metadata) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        putInstant(Fields.TIMESTAMP, value.timestamp)
        setValue(Fields.CONTEXT, value.context, Event.Context.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {
        val id = getValue(Fields.ID, Id.jsonSerde)
        val timestamp = getRequiredInstant(Fields.TIMESTAMP)
        val context = getValue(Fields.CONTEXT, Event.Context.jsonSerde)
        Event.Metadata(id, timestamp, context)
    }

    private object Fields {
        const val ID = "id"
        const val TIMESTAMP = "timestamp"
        const val CONTEXT = "context"
    }
}

val Event.Metadata.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Metadata> get() = EventMetadataJsonSerde
