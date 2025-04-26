package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import org.json.JSONObject

private object ActorAccountJsonSerde : JsonSerde.SchemaAware<Actor.Account> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/Account.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.Account) = when (value) {
        is Actor.UserAccount -> Actor.UserAccount.jsonSerde.serialize(value)
        is Actor.ServiceAccount -> Actor.ServiceAccount.jsonSerde.serialize(value)
    }

    override fun deserialize(value: JSONObject) = when (val type = value.getRequiredString(Fields.TYPE)) {
        UserAccountJsonSerde.TYPE_VALUE -> Actor.UserAccount.jsonSerde.deserialize(value)
        ServiceAccountJsonSerde.TYPE_VALUE -> Actor.ServiceAccount.jsonSerde.deserialize(value)
        else -> error("Unsupported actor account type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Actor.Account.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.Account> get() = ActorAccountJsonSerde