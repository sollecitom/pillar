package sollecitom.libs.pillar.avro.serialization.core.locale

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.pillar.acme.conventions.toAcmeLanguageTag
import sollecitom.libs.pillar.acme.conventions.toAcmeLocale
import java.util.*

/** Avro serializer/deserializer for [Locale], using language tags. Handles the problematic Norwegian locale (`no_NO_NY`) as a special case. */
val localeAvroSerde: AvroSerde<Locale> get() = LocaleAvroSerde

private object LocaleAvroSerde : AvroSerde<Locale> {

    override val schema get() = LocaleAvroSchemas.locale

    override fun serialize(value: Locale): GenericRecord = buildRecord {

        set(Fields.tag, value.toAcmeLanguageTag())
    }

    override fun deserialize(value: GenericRecord): Locale = with(value) {

        getString(Fields.tag).toAcmeLocale()
    }

    private object Fields {
        const val tag = "tag"
    }
}