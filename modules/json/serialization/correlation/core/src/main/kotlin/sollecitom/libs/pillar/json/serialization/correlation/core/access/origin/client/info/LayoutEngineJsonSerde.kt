package sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.web.client.info.domain.LayoutEngine
import org.json.JSONObject

private object LayoutEngineJsonSerde : JsonSerde.SchemaAware<LayoutEngine> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/origin/client/info/layout-engine/LayoutEngine.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: LayoutEngine) = JSONObject().apply {
        value.className?.value?.let { put(Fields.CLASS_NAME, it) }
        value.name?.value?.let { put(Fields.NAME, it) }
        value.version?.let { put(Fields.VERSION, it.value) }
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val className = getStringOrNull(Fields.CLASS_NAME)?.let(::Name)
        val name = getStringOrNull(Fields.NAME)?.let(::Name)
        val version = getStringOrNull(Fields.VERSION)?.let(::Name)
        LayoutEngine(className, name, version)
    }

    private object Fields {
        const val CLASS_NAME = "class-name"
        const val NAME = "name"
        const val VERSION = "version"
    }
}

val LayoutEngine.Companion.jsonSerde: JsonSerde.SchemaAware<LayoutEngine> get() = LayoutEngineJsonSerde