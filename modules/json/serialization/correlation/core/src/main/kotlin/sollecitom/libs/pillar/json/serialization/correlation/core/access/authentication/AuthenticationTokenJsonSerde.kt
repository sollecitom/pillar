package sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.json.utils.getInstantOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object AuthenticationTokenJsonSerde : JsonSerde.SchemaAware<Authentication.Token> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/authentication/AuthenticationToken.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Authentication.Token) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.VALID_FROM, value.validFrom?.toString())
        put(Fields.VALID_TO, value.validTo?.toString())
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        val validFrom = getInstantOrNull(Fields.VALID_FROM)
        val validTo = getInstantOrNull(Fields.VALID_TO)
        Authentication.Token(id = id, validFrom = validFrom, validTo = validTo)
    }

    private object Fields {
        const val ID = "id"
        const val VALID_FROM = "valid-from"
        const val VALID_TO = "valid-to"
    }
}

val Authentication.Token.Companion.jsonSerde: JsonSerde.SchemaAware<Authentication.Token> get() = AuthenticationTokenJsonSerde