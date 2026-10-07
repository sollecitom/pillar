package sollecitom.libs.pillar.json.serialization.correlation.core

import java.util.Locale

@Suppress("DEPRECATION")
private val problematicNorwegianLocale = Locale("no", "NO", "NY")
private const val PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG = "no_NO_NY"

/** The language tag for this locale. `no_NO_NY` is special-cased, because its standard tag (`nn-NO`) parses back to a different locale. */
internal fun Locale.toAcmeLanguageTag(): String = if (this == problematicNorwegianLocale) PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG else toLanguageTag()

/** The locale for a tag written by [toAcmeLanguageTag]. */
internal fun String.toAcmeLocale(): Locale = if (this == PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG) problematicNorwegianLocale else Locale.forLanguageTag(this)
