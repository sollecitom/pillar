package sollecitom.libs.pillar.json.serialization.core.file

import sollecitom.libs.swissknife.core.domain.file.FileContent
import sollecitom.libs.swissknife.core.domain.file.withName
import sollecitom.libs.swissknife.json.utils.getBytesFromBase64String
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.putBytesAsBase64String
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

val FileContent.Inline.Companion.jsonSerde: JsonSerde.SchemaAware<FileContent.Inline> get() = InlineFileContentJsonSerde

internal object InlineFileContentJsonSerde : JsonSerde.SchemaAware<FileContent.Inline> {

    const val TYPE_VALUE = "inline"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/file/InlineFileContent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FileContent.Inline) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        putBytesAsBase64String(Fields.BYTES, value.bytes)
        put(Fields.FORMAT, value.format.name)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'" }
        val format = getRequiredString(Fields.FORMAT).let(FileContent.Format::withName)
        val bytes = getBytesFromBase64String(Fields.BYTES)
        FileContent.Inline(bytes, format)
    }

    private object Fields {
        const val TYPE = "type"
        const val BYTES = "bytes"
        const val FORMAT = "format"
    }
}