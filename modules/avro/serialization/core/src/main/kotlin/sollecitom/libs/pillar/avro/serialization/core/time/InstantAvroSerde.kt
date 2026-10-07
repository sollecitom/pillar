package sollecitom.libs.pillar.avro.serialization.core.time

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getLong
import kotlin.time.Instant

/** Avro schema for [Instant] timestamps. */
val Instant.Companion.avroSchema get() = TimeAvroSchemas.timestamp
/** Avro serializer/deserializer for [Instant], as nanoseconds since the epoch (the `timestamp-nanos` logical type). */
val Instant.Companion.avroSerde: AvroSerde<Instant> get() = InstantAvroSerde

private object InstantAvroSerde : AvroSerde<Instant> {

    private const val NANOS_PER_SECOND = 1_000_000_000L
    override val schema get() = Instant.avroSchema

    override fun serialize(value: Instant): GenericRecord = buildRecord {

        set(Fields.value, value.epochNanoseconds)
    }

    override fun deserialize(value: GenericRecord) = with(value) {

        val epochNanoseconds = getLong(Fields.value)
        Instant.fromEpochSeconds(Math.floorDiv(epochNanoseconds, NANOS_PER_SECOND), Math.floorMod(epochNanoseconds, NANOS_PER_SECOND))
    }

    private val Instant.epochNanoseconds: Long get() = Math.multiplyExact(epochSeconds, NANOS_PER_SECOND).let { Math.addExact(it, nanosecondsOfSecond.toLong()) }

    private object Fields {
        const val value = "value"
    }
}
