package sollecitom.libs.pillar.avro.serialization.test.utils

import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import sollecitom.libs.pillar.avro.serialization.core.UNKNOWN_ENUM_SYMBOL
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde

/** Serializes [value], then replaces the enum symbol in [fieldName] with [UNKNOWN_ENUM_SYMBOL], as a reader sees a symbol added after it was built. */
fun <VALUE : Any> AvroSerde<VALUE>.serializeWithUnknownEnumSymbol(value: VALUE, fieldName: String): GenericRecord = serialize(value).apply { put(fieldName, unknownEnumSymbolFor(fieldName)) }

private fun AvroSerde<*>.unknownEnumSymbolFor(fieldName: String) = schema.getField(fieldName).schema().let { GenericData.EnumSymbol(it, UNKNOWN_ENUM_SYMBOL) }
