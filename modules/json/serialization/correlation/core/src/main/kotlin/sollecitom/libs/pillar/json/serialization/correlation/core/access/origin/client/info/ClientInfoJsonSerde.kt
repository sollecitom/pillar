package sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.json.utils.serde.getValue
import sollecitom.libs.swissknife.json.utils.serde.setValue
import sollecitom.libs.swissknife.web.client.info.domain.*
import org.json.JSONObject

private object ClientInfoJsonSerde : JsonSerde.SchemaAware<ClientInfo> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/origin/client/info/ClientInfo.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: ClientInfo) = JSONObject().apply {
        setValue(Fields.DEVICE, value.device, Device.jsonSerde)
        setValue(Fields.OPERATING_SYSTEM, value.operatingSystem, OperatingSystem.jsonSerde)
        setValue(Fields.LAYOUT_ENGINE, value.layoutEngine, LayoutEngine.jsonSerde)
        setValue(Fields.AGENT, value.agent, Agent.jsonSerde)
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val device = getValue(Fields.DEVICE, Device.jsonSerde)
        val operatingSystem = getValue(Fields.OPERATING_SYSTEM, OperatingSystem.jsonSerde)
        val layoutEngine = getValue(Fields.LAYOUT_ENGINE, LayoutEngine.jsonSerde)
        val agent = getValue(Fields.AGENT, Agent.jsonSerde)
        ClientInfo(device, operatingSystem, layoutEngine, agent)
    }

    private object Fields {
        const val DEVICE = "device"
        const val OPERATING_SYSTEM = "operating-system"
        const val LAYOUT_ENGINE = "layout-engine"
        const val AGENT = "agent"
    }
}

val ClientInfo.Companion.jsonSerde: JsonSerde.SchemaAware<ClientInfo> get() = ClientInfoJsonSerde