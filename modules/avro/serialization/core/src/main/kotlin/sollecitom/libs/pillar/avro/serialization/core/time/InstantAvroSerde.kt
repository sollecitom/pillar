package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import kotlinx.datetime.Instant
import org.apache.avro.generic.GenericRecord

val Instant.Companion.avroSchema get() = TimeAvroSchemas.timestamp
val Instant.Companion.avroSerde: AvroSerde<Instant> get() = InstantAvroSerde

private object InstantAvroSerde : AvroSerde<Instant> {

    private const val ISO_8601_FORMAT = "ISO_8601"
    override val schema get() = Instant.avroSchema

    override fun serialize(value: Instant): GenericRecord = buildRecord {

        set(Fields.value, value.toString())
        setEnum(Fields.format, ISO_8601_FORMAT)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val format = getEnum(Fields.format)
        check(format == ISO_8601_FORMAT) { "Expected format to be '$ISO_8601_FORMAT' but was '$format'" }
        val stringValue = getString(Fields.value)
        Instant.parse(stringValue)
    }

    private object Fields {
        const val value = "value"
        const val format = "format"
    }
}