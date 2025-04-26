package sollecitom.libs.pillar.json.serialization.ddd.happening

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.json.utils.getRequiredInt
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

internal object HappeningTypeJsonSerde : JsonSerde.SchemaAware<Happening.Type> {

    private const val SCHEMA_LOCATION = "/json/schemas/acme/common/happening/HappeningType.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Happening.Type) = JSONObject().apply {
        put(Fields.NAME, value.name.value)
        put(Fields.VERSION, value.version.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val name = getRequiredString(Fields.NAME).let(::Name)
        val version = getRequiredInt(Fields.VERSION).let(::IntVersion)
        Happening.Type(name, version)
    }

    private object Fields {
        const val NAME = "name"
        const val VERSION = "version"
    }
}

val Happening.Type.Companion.jsonSerde: JsonSerde.SchemaAware<Happening.Type> get() = HappeningTypeJsonSerde