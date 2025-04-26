package sollecitom.libs.pillar.json.serialization.web.api

import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.web.api.domain.error.ApiError
import org.json.JSONObject

private object ApiErrorJsonSerde : JsonSerde.SchemaAware<ApiError> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/web/api/ApiError.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ApiError) = JSONObject().apply {
        put(Fields.MESSAGE, value.message)
        put(Fields.CODE, value.code)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val message = getRequiredString(Fields.MESSAGE)
        val code = getRequiredString(Fields.CODE)
        ApiError(message = message, code = code)
    }

    private object Fields {
        const val MESSAGE = "message"
        const val CODE = "code"
    }
}

val ApiError.Companion.jsonSerde: JsonSerde.SchemaAware<ApiError> get() = ApiErrorJsonSerde