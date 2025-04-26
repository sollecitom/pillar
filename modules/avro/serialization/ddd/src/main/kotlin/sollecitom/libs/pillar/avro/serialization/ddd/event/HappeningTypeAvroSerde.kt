package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getInt
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.ddd.domain.Happening
import org.apache.avro.generic.GenericRecord

val Happening.Type.Companion.avroSchema get() = EventAvroSchemas.happeningType
val Happening.Type.Companion.avroSerde: AvroSerde<Happening.Type> get() = HappeningTypeAvroSerde

private object HappeningTypeAvroSerde : AvroSerde<Happening.Type> {

    override val schema get() = Happening.Type.avroSchema

    override fun serialize(value: Happening.Type): GenericRecord = buildRecord {

        set(Fields.name, value.name.value)
        set(Fields.version, value.version.value)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val name = getString(Fields.name).let(::Name)
        val version = getInt(Fields.version).let(::IntVersion)
        Happening.Type(name = name, version = version)
    }

    private object Fields {
        const val name = "name"
        const val version = "version"
    }
}