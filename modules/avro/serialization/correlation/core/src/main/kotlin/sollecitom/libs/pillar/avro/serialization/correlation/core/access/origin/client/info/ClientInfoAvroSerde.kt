package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getValue
import sollecitom.libs.swissknife.avro.serialization.utils.setValue
import sollecitom.libs.swissknife.web.client.info.domain.*
import org.apache.avro.generic.GenericRecord

val ClientInfo.Companion.avroSchema get() = ClientInfoAvroSchemas.clientInfo
val ClientInfo.Companion.avroSerde: AvroSerde<ClientInfo> get() = ClientInfoAvroSerde

private object ClientInfoAvroSerde : AvroSerde<ClientInfo> {

    override val schema get() = ClientInfo.avroSchema

    override fun serialize(value: ClientInfo): GenericRecord = buildRecord {

        setValue(Fields.device, value.device, Device.avroSerde)
        setValue(Fields.agent, value.agent, Agent.avroSerde)
        setValue(Fields.layoutEngine, value.layoutEngine, LayoutEngine.avroSerde)
        setValue(Fields.operatingSystem, value.operatingSystem, OperatingSystem.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val device = getValue(Fields.device, Device.avroSerde)
        val agent = getValue(Fields.agent, Agent.avroSerde)
        val layoutEngine = getValue(Fields.layoutEngine, LayoutEngine.avroSerde)
        val operatingSystem = getValue(Fields.operatingSystem, OperatingSystem.avroSerde)
        ClientInfo(device = device, operatingSystem = operatingSystem, layoutEngine = layoutEngine, agent = agent)
    }

    private object Fields {
        const val device = "device"
        const val agent = "agent"
        const val layoutEngine = "layout_engine"
        const val operatingSystem = "operating_system"
    }
}