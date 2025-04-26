package sollecitom.libs.pillar.json.serialization.correlation.core.access

import sollecitom.libs.pillar.json.serialization.correlation.core.access.actor.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.scope.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object AuthenticatedAccessJsonSerde : JsonSerde.SchemaAware<Access.Authenticated> {

    const val TYPE_VALUE = "authenticated"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/AuthenticatedAccess.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access.Authenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ACTOR, value.actor, Actor.jsonSerde)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
        setValue(Fields.SCOPE, value.scope, AccessScope.jsonSerde)
        put(Fields.IS_TEST, value.isTest)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val actor = getValue(Fields.ACTOR, Actor.jsonSerde)
        val origin = getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        val scope = getValue(Fields.SCOPE, AccessScope.jsonSerde)
        Access.Authenticated(actor = actor, origin = origin, authorization = authorization, scope = scope)
    }

    private object Fields {
        const val TYPE = "type"
        const val ACTOR = "actor"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
        const val SCOPE = "scope"
        const val IS_TEST = "is-test"
    }
}

val Access.Authenticated.Companion.jsonSerde: JsonSerde.SchemaAware<Access.Authenticated> get() = AuthenticatedAccessJsonSerde