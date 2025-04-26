package sollecitom.libs.pillar.json.serialization.ddd.event

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.pillar.json.serialization.ddd.happening.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.json.utils.getRequiredInstant
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.putInstant
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object EventReferenceJsonSerde : JsonSerde.SchemaAware<Event.Reference> {

    private const val SCHEMA_LOCATION = "/json/schemas/acme/common/event/EventReference.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Reference) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        putInstant(Fields.TIMESTAMP, value.timestamp)
        setValue(Fields.TYPE, value.type, Happening.Type.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        val timestamp = getRequiredInstant(Fields.TIMESTAMP)
        val type = getValue(Fields.TYPE, Happening.Type.jsonSerde)
        Event.Reference(id, type, timestamp)
    }

    private object Fields {
        const val ID = "id"
        const val TYPE = "type"
        const val TIMESTAMP = "timestamp"
    }
}

val Event.Reference.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Reference> get() = EventReferenceJsonSerde