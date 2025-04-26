package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.correlation.core.access.authentication.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object ImpersonatingActorJsonSerde : JsonSerde.SchemaAware<ImpersonatingActor> {

    const val TYPE_VALUE = "impersonating"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/actor/ImpersonatingActor.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ImpersonatingActor) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.IMPERSONATOR, value.impersonator, Actor.Account.jsonSerde)
        setValue(Fields.IMPERSONATED, value.impersonated, Actor.Account.jsonSerde)
        setValue(Fields.AUTHENTICATION, value.authentication, Authentication.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val impersonator = getValue(Fields.IMPERSONATOR, Actor.Account.jsonSerde)
        val impersonated = getValue(Fields.IMPERSONATED, Actor.Account.jsonSerde)
        val authentication = getValue(Fields.AUTHENTICATION, Authentication.jsonSerde)
        ImpersonatingActor(impersonator = impersonator, impersonated = impersonated, authentication = authentication)
    }

    private object Fields {
        const val TYPE = "type"
        const val IMPERSONATOR = "impersonator"
        const val IMPERSONATED = "impersonated"
        const val AUTHENTICATION = "authentication"
    }
}

val ImpersonatingActor.Companion.jsonSerde: JsonSerde.SchemaAware<ImpersonatingActor> get() = ImpersonatingActorJsonSerde