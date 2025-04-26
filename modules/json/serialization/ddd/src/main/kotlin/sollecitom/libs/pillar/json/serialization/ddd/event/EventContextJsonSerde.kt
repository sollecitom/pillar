package sollecitom.libs.pillar.json.serialization.ddd.event

import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.getValueOrNull
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object EventContextJsonSerde : JsonSerde.SchemaAware<Event.Context> {

    private const val SCHEMA_LOCATION = "/json/schemas/acme/common/event/EventContext.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event.Context) = JSONObject().apply {
        setValue(Fields.INVOCATION, value.invocation, InvocationContext.jsonSerde)
        value.parent?.let { setValue(Fields.PARENT, it, Event.Reference.jsonSerde) }
        value.originating?.let { setValue(Fields.ORIGINATING, it, Event.Reference.jsonSerde) }
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val invocation = getValue(Fields.INVOCATION, InvocationContext.jsonSerde)
        val parent = getValueOrNull(Fields.PARENT, Event.Reference.jsonSerde)
        val originating = getValueOrNull(Fields.ORIGINATING, Event.Reference.jsonSerde)
        Event.Context(invocation, parent, originating)
    }

    private object Fields {
        const val INVOCATION = "invocation"
        const val PARENT = "parent"
        const val ORIGINATING = "originating"
    }
}

val Event.Context.Companion.jsonSerde: JsonSerde.SchemaAware<Event.Context> get() = EventContextJsonSerde