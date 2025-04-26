package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getStringOrNull
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.web.client.info.domain.Device
import org.apache.avro.generic.GenericRecord

val Device.Companion.avroSchema get() = ClientInfoAvroSchemas.device
val Device.Companion.avroSerde: AvroSerde<Device> get() = DeviceAvroSerde

private object DeviceAvroSerde : AvroSerde<Device> {

    override val schema get() = Device.avroSchema

    override fun serialize(value: Device): GenericRecord = buildRecord {

        set(Fields.className, value.className?.value)
        set(Fields.brand, value.brand?.value)
        set(Fields.name, value.name?.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val className = getStringOrNull(Fields.className)?.let(::Name)
        val brand = getStringOrNull(Fields.brand)?.let(::Name)
        val name = getStringOrNull(Fields.name)?.let(::Name)
        Device(className = className, name = name, brand = brand)
    }

    private object Fields {
        const val className = "class_name"
        const val brand = "brand"
        const val name = "name"
    }
}