package sollecitom.libs.pillar.json.serialization.pagination

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.json.utils.getRequiredLong
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.pagination.domain.Pagination
import org.json.JSONObject

internal object PaginationInformationJsonSerde : JsonSerde.SchemaAware<Pagination.Information> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/pagination/PaginationInformation.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Pagination.Information) = JSONObject().apply {
        put(Fields.TOTAL_ITEMS_COUNT, value.totalItemsCount)
        put(Fields.CONTINUATION_TOKEN, value.continuationToken?.value)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val totalElementsCount = getRequiredLong(Fields.TOTAL_ITEMS_COUNT)
        val continuationToken = getStringOrNull(Fields.CONTINUATION_TOKEN)?.let(::Name)
        Pagination.Information(totalItemsCount = totalElementsCount, continuationToken = continuationToken)
    }

    private object Fields {
        const val TOTAL_ITEMS_COUNT = "total-items-count"
        const val CONTINUATION_TOKEN = "continuation-token"
    }
}

val Pagination.Information.Companion.jsonSerde: JsonSerde.SchemaAware<Pagination.Information> get() = PaginationInformationJsonSerde