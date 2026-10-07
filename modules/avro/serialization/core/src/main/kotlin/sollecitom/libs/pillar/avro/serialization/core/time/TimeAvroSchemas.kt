package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import org.apache.avro.Schema

/** Avro schema catalogue for time-related types (Timestamp, Month, MonthAndYear). */
object TimeAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.time") {

    val timestamp: Schema by lazy { getSchema(name = "Timestamp") }
    val month: Schema by lazy { getSchema(name = "Month") }
    val monthAndYear: Schema by lazy { getSchema(name = "MonthAndYear", dependencies = setOf(month)) }

    override val all: Sequence<Schema> get() = sequenceOf(timestamp, month, monthAndYear)
}