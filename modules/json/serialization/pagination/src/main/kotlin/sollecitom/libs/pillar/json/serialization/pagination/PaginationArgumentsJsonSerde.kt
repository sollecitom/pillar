package sollecitom.libs.pillar.json.serialization.pagination

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.json.utils.getRequiredInt
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.pagination.domain.Pagination
import org.json.JSONObject

internal object PaginationArgumentsJsonSerde : JsonSerde.SchemaAware<Pagination.Arguments> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/pagination/PaginationArguments.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Pagination.Arguments) = JSONObject().apply {
        put(Fields.LIMIT, value.limit)
        put(Fields.CONTINUATION_TOKEN, value.continuationToken?.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val limit = getRequiredInt(Fields.LIMIT)
        val continuationToken = getStringOrNull(Fields.CONTINUATION_TOKEN)?.let(::Name)
        Pagination.Arguments(limit = limit, continuationToken = continuationToken)
    }

    private object Fields {
        const val LIMIT = "limit"
        const val CONTINUATION_TOKEN = "continuation-token"
    }
}

val Pagination.Arguments.Companion.jsonSerde: JsonSerde.SchemaAware<Pagination.Arguments> get() = PaginationArgumentsJsonSerde