package sollecitom.libs.pillar.json.serialization.correlation.core.access.origin

import sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.client.info.jsonSerde
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo
import org.json.JSONObject

private object OriginJsonSerde : JsonSerde.SchemaAware<Origin> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/origin/Origin.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Origin) = JSONObject().apply {
        put(Fields.IP_ADDRESS, value.ipAddress?.stringValue)
        setValue(Fields.CLIENT_INFO, value.clientInfo, ClientInfo.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val ipAddress = getStringOrNull(Fields.IP_ADDRESS)?.let(IpAddress::create)
        val clientInfo = getValue(Fields.CLIENT_INFO, ClientInfo.jsonSerde)
        Origin(ipAddress = ipAddress, clientInfo = clientInfo)
    }

    private object Fields {
        const val IP_ADDRESS = "ip-address"
        const val CLIENT_INFO = "client-info"
    }
}

val Origin.Companion.jsonSerde: JsonSerde.SchemaAware<Origin> get() = OriginJsonSerde