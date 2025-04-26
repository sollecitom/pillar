package sollecitom.libs.pillar.json.serialization.core.file

import sollecitom.libs.swissknife.core.domain.file.FileContent
import sollecitom.libs.swissknife.core.domain.file.withName
import sollecitom.libs.swissknife.json.utils.getRequiredInt
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject
import java.net.URI

val FileContent.Remote.Companion.jsonSerde: JsonSerde.SchemaAware<FileContent.Remote> get() = RemoteFileContentJsonSerde

internal object RemoteFileContentJsonSerde : JsonSerde.SchemaAware<FileContent.Remote> {

    const val TYPE_VALUE = "remote"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/file/RemoteFileContent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FileContent.Remote) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        put(Fields.URI, value.contentURI.toString())
        put(Fields.LENGTH, value.length)
        put(Fields.FORMAT, value.format.name)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'" }
        val format = getRequiredString(Fields.FORMAT).let(FileContent.Format::withName)
        val contentURI = getRequiredString(Fields.URI).let(::URI)
        val length = getRequiredInt(Fields.LENGTH)
        FileContent.Remote(length = length, contentURI = contentURI, format = format)
    }

    private object Fields {
        const val TYPE = "type"
        const val FORMAT = "format"
        const val LENGTH = "length"
        const val URI = "uri"
    }
}