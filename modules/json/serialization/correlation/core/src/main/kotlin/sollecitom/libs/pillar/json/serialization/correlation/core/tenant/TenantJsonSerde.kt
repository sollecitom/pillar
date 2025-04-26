package sollecitom.libs.pillar.json.serialization.correlation.core.tenant

import sollecitom.libs.pillar.json.serialization.core.identity.jsonSerde
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import org.json.JSONObject

private object TenantJsonSerde : JsonSerde.SchemaAware<Tenant> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/tenant/Tenant.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Tenant) = JSONObject().apply {
        setValue(Fields.ID, value.id, Id.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val id = getValue(Fields.ID, Id.jsonSerde)
        Tenant(id = id)
    }

    private object Fields {
        const val ID = "id"
    }
}

val Tenant.Companion.jsonSerde: JsonSerde.SchemaAware<Tenant> get() = TenantJsonSerde