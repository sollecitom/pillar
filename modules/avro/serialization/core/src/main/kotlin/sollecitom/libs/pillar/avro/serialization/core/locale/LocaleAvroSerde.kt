package sollecitom.libs.pillar.avro.serialization.core.locale

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.buildRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getString
import org.apache.avro.generic.GenericRecord
import java.util.*

val localeAvroSerde: AvroSerde<Locale> get() = LocaleAvroSerde

private object LocaleAvroSerde : AvroSerde<Locale> {

    @Suppress("DEPRECATION")
    private val problematicNorwegianLocale = Locale("no", "NO", "NY")
    private const val problematicNorwegianLanguageTag = "no_NO_NY"

    override val schema get() = LocaleAvroSchemas.locale

    override fun serialize(value: Locale): GenericRecord = buildRecord {

        set(Fields.tag, toLanguageTag(value))
    }

    override fun deserialize(value: GenericRecord): Locale = with(value) {

        val tag = getString(Fields.tag)
        fromLanguageTag(tag)
    }

    private fun fromLanguageTag(languageTag: String) = when (languageTag) {
        problematicNorwegianLanguageTag -> problematicNorwegianLocale
        else -> Locale.forLanguageTag(languageTag)
    }

    private fun toLanguageTag(locale: Locale) = when (locale) {
        problematicNorwegianLocale -> problematicNorwegianLanguageTag
        else -> locale.toLanguageTag()
    }

    private object Fields {
        const val tag = "tag"
    }
}