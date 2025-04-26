package sollecitom.libs.pillar.json.serialization.core.identity

import sollecitom.libs.swissknife.core.domain.identity.*
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object IdJsonSerde : JsonSerde.SchemaAware<Id> {

    private const val TYPE_ULID = "ulid"
    private const val TYPE_KSUID = "ksuid"
    private const val TYPE_UUID = "uuid"
    private const val TYPE_STRING = "string"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/identity/Id.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Id) = JSONObject().apply {
        put(Fields.VALUE, value.stringValue)
        val type = when (value) {
            is ULID -> TYPE_ULID
            is KSUID -> TYPE_KSUID
            is UUID -> TYPE_UUID
            is StringId -> TYPE_STRING
        }
        put(Fields.TYPE, type)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        val stringValue = getRequiredString(Fields.VALUE)
        when (type) {
            TYPE_ULID -> ULID(stringValue)
            TYPE_KSUID -> KSUID(stringValue)
            TYPE_UUID -> UUID(stringValue)
            TYPE_STRING -> StringId(stringValue)
            else -> error("Unknown ID type $type")
        }
    }

    private object Fields {
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val Id.Companion.jsonSerde: JsonSerde.SchemaAware<Id> get() = IdJsonSerde