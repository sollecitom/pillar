package sollecitom.libs.pillar.json.serialization.correlation.core.access.session

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.idp.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object FederatedSessionJsonSerde : JsonSerde.SchemaAware<FederatedSession> {

    const val TYPE_VALUE = "federated"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/session/FederatedSession.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: FederatedSession) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        setValue(Fields.IDENTITY_PROVIDER, value.identityProvider, IdentityProvider.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '${TYPE_VALUE}'" }
        val id = getValue(Fields.ID, Id.jsonSerde)
        val identityProvider = getValue(Fields.IDENTITY_PROVIDER, IdentityProvider.jsonSerde)
        FederatedSession(id = id, identityProvider = identityProvider)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val IDENTITY_PROVIDER = "identity-provider"
    }
}

val FederatedSession.Companion.jsonSerde: JsonSerde.SchemaAware<FederatedSession> get() = FederatedSessionJsonSerde