package sollecitom.libs.pillar.json.serialization.correlation.core.access.scope

import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValues
import sollecitom.libs.swissknife.json.utils.serde.setValues
import org.json.JSONObject

private object AccessScopeJsonSerde : JsonSerde.SchemaAware<AccessScope> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/scope/AccessScope.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: AccessScope) = JSONObject().apply {
        setValues(Fields.CONTAINER_STACK, value.containers, AccessContainer.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val containerStack = getValues(Fields.CONTAINER_STACK, AccessContainer.jsonSerde)
        AccessScope(containerStack = containerStack)
    }

    private object Fields {
        const val CONTAINER_STACK = "container-stack"
    }
}

val AccessScope.Companion.jsonSerde: JsonSerde.SchemaAware<AccessScope> get() = AccessScopeJsonSerde