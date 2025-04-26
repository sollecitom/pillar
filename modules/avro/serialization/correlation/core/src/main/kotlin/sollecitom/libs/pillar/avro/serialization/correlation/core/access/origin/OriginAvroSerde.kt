package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info.avroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.*
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo
import org.apache.avro.generic.GenericRecord

val Origin.Companion.avroSchema get() = OriginAvroSchemas.origin
val Origin.Companion.avroSerde: AvroSerde<Origin> get() = OriginAvroSerde

private object OriginAvroSerde : AvroSerde<Origin> {

    override val schema get() = Origin.avroSchema

    override fun serialize(value: Origin): GenericRecord = buildRecord {

        set(Fields.ipAddress, value.ipAddress?.stringValue)
        setValue(Fields.clientInfo, value.clientInfo, ClientInfo.avroSerde)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val ipAddress = getStringOrNull(Fields.ipAddress)?.let(IpAddress.Companion::create)
        val clientInfo = getValue(Fields.clientInfo, ClientInfo.avroSerde)
        Origin(ipAddress = ipAddress, clientInfo = clientInfo)
    }

    private object Fields {
        const val ipAddress = "ip_address"
        const val clientInfo = "client_info"
    }
}