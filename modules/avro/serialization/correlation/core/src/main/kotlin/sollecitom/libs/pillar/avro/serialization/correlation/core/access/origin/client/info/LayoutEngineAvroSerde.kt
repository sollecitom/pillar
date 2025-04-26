package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getStringOrNull
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.web.client.info.domain.LayoutEngine
import org.apache.avro.generic.GenericRecord

val LayoutEngine.Companion.avroSchema get() = ClientInfoAvroSchemas.layoutEngine
val LayoutEngine.Companion.avroSerde: AvroSerde<LayoutEngine> get() = LayoutEngineAvroSerde

private object LayoutEngineAvroSerde : AvroSerde<LayoutEngine> {

    override val schema get() = LayoutEngine.avroSchema

    override fun serialize(value: LayoutEngine): GenericRecord = buildRecord {

        set(Fields.className, value.className?.value)
        set(Fields.name, value.name?.value)
        set(Fields.version, value.version?.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val className = getStringOrNull(Fields.className)?.let(::Name)
        val version = getStringOrNull(Fields.version)?.let(::Name)
        val name = getStringOrNull(Fields.name)?.let(::Name)
        LayoutEngine(className = className, name = name, version = version)
    }

    private object Fields {
        const val className = "class_name"
        const val name = "name"
        const val version = "version"
    }
}