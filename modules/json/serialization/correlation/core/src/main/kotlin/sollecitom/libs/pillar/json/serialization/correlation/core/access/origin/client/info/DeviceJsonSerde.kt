package sollecitom.libs.pillar.json.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.json.utils.getStringOrNull
import sollecitom.libs.swissknife.json.utils.jsonSchemaAt
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.web.client.info.domain.Device
import org.json.JSONObject

private object DeviceJsonSerde : JsonSerde.SchemaAware<Device> {

    private const val SCHEMA_LOCATION = "json/schemas/acme/common/correlation/access/origin/client/info/device/Device.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Device) = JSONObject().apply {
        value.className?.value?.let { put(Fields.CLASS_NAME, it) }
        value.name?.value?.let { put(Fields.NAME, it) }
        value.brand?.value?.let { put(Fields.BRAND, it) }
    }

    override fun deserialize(value: JSONObject) = with(value) {

        val className = getStringOrNull(Fields.CLASS_NAME)?.let(::Name)
        val name = getStringOrNull(Fields.NAME)?.let(::Name)
        val brand = getStringOrNull(Fields.BRAND)?.let(::Name)
        Device(className, name, brand)
    }

    private object Fields {
        const val CLASS_NAME = "class-name"
        const val NAME = "name"
        const val BRAND = "brand"
    }
}

val Device.Companion.jsonSerde: JsonSerde.SchemaAware<Device> get() = DeviceJsonSerde