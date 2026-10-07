package sollecitom.libs.pillar.acme.conventions

import java.util.Locale

private val problematicNorwegianLocale = Locale.of("no", "NO", "NY")
private const val PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG = "no_NO_NY"

fun Locale.toAcmeLanguageTag(): String = if (this == problematicNorwegianLocale) PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG else toLanguageTag()

fun String.toAcmeLocale(): Locale = if (this == PROBLEMATIC_NORWEGIAN_LANGUAGE_TAG) problematicNorwegianLocale else Locale.forLanguageTag(this)
