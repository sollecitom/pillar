package sollecitom.libs.pillar.json.serialization.core.file

import sollecitom.libs.swissknife.core.domain.file.FileContent
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

val FileContent.Companion.jsonSerde: JsonSerde.SchemaAware<FileContent> get() = FileContentJsonSerde

private object FileContentJsonSerde : JsonSerde.SchemaAware<FileContent> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/file/FileContent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FileContent) = when (value) {
        is FileContent.Inline -> FileContent.Inline.jsonSerde.serialize(value)
        is FileContent.Remote -> FileContent.Remote.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject): FileContent = with(value) {

        when (val type = getRequiredString(Fields.TYPE)) {
            InlineFileContentJsonSerde.TYPE_VALUE -> FileContent.Inline.jsonSerde.deserialize(value)
            RemoteFileContentJsonSerde.TYPE_VALUE -> FileContent.Remote.jsonSerde.deserialize(value)
            else -> error("Invalid file content type '$type'")
        }
    }

    private object Fields {
        const val TYPE = "type"
    }
}