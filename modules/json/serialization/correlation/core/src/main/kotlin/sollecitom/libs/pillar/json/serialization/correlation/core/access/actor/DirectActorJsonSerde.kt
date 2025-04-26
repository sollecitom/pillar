package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object DirectActorJsonSerde : JsonSerde.SchemaAware<DirectActor> {

    const val TYPE_VALUE = "direct"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/DirectActor.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: DirectActor) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACCOUNT, value.account, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val account = getValue(Fields.ACCOUNT, Actor.Account.jsonSerde)
        val authentication = getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
        DirectActor(account = account, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACCOUNT = "account"
        const val AUTHENTICATION = "authentication"
    }
}

val DirectActor.Companion.jsonSerde: JsonSerde.SchemaAware<DirectActor> get() = DirectActorJsonSerde