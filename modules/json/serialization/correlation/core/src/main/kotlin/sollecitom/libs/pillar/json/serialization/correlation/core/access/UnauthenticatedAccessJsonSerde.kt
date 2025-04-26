package sollecitom.libs.pillar.json.serialization.correlation.core.access

import sollecitom.libs.pillar.json.serialization.correlation.core.access.autorization.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.jsonSerde
import sollecitom.libs.pillar.json.serialization.correlation.core.access.scope.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.json.utils.getRequiredString
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

internal object UnauthenticatedAccessJsonSerde : JsonSerde.SchemaAware<Access.Unauthenticated> {

    const val TYPE_VALUE = "unauthenticated"
    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/UnauthenticatedAccess.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Access.Unauthenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
        setValue(Fields.SCOPE, value.scope, AccessScope.jsonSerde)
        put(Fields.IS_TEST, value.isTest)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val type = getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val origin = getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        val scope = getValue(Fields.SCOPE, AccessScope.jsonSerde)
        val isTest = getBoolean(Fields.IS_TEST)
        Access.Unauthenticated(origin = origin, authorization = authorization, scope = scope, isTest = isTest)
    }

    private object Fields {
        const val TYPE = "type"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
        const val SCOPE = "scope"
        const val IS_TEST = "is-test"
    }
}

val Access.Unauthenticated.Companion.jsonSerde: JsonSerde.SchemaAware<Access.Unauthenticated> get() = UnauthenticatedAccessJsonSerde