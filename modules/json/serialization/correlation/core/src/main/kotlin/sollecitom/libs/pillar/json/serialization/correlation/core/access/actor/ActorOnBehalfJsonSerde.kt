package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object ActorOnBehalfJsonSerde : JsonSerde.SchemaAware<ActorOnBehalf> {

    const val TYPE_VALUE = "on-behalf"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/ActorOnBehalf.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ActorOnBehalf) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACCOUNT, value.account, Actor.Account.jsonSerde)
        setValue(Fields.BENEFITING_ACCOUNT, value.benefitingAccount, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val account = getValue(Fields.ACCOUNT, Actor.Account.jsonSerde)
        val benefitingAccount = getValue(Fields.BENEFITING_ACCOUNT, Actor.Account.jsonSerde)
        val authentication = getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
        ActorOnBehalf(account = account, benefitingAccount = benefitingAccount, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACCOUNT = "account"
        const val BENEFITING_ACCOUNT = "benefiting-account"
        const val AUTHENTICATION = "authentication"
    }
}

val ActorOnBehalf.Companion.jsonSerde: JsonSerde.SchemaAware<ActorOnBehalf> get() = ActorOnBehalfJsonSerde