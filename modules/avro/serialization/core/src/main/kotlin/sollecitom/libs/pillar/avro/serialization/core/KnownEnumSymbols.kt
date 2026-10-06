package sollecitom.libs.pillar.avro.serialization.core

import org.apache.avro.generic.GenericRecord
import sollecitom.libs.swissknife.avro.serialization.utils.getEnum

/** The symbol every Acme Avro enum declares as its default, so a reader decodes symbols added after it was built as this one. */
const val UNKNOWN_ENUM_SYMBOL = "UNKNOWN"

/** Returns the enum symbol in [fieldName], failing if it's [UNKNOWN_ENUM_SYMBOL]. */
fun GenericRecord.getKnownEnum(fieldName: String): String = getEnum(fieldName).also { if (it == UNKNOWN_ENUM_SYMBOL) failOnUnknownEnumSymbol(fieldName) }

/** Fails because [fieldName] holds [UNKNOWN_ENUM_SYMBOL]: the writer used a symbol this reader doesn't know. */
fun failOnUnknownEnumSymbol(fieldName: String): Nothing = error("Field '$fieldName' is '$UNKNOWN_ENUM_SYMBOL': the writer used a symbol this reader doesn't know")
