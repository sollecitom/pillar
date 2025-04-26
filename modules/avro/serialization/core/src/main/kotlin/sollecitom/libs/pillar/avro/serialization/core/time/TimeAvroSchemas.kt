package sollecitom.libs.pillar.avro.serialization.core.time

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object TimeAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.time") {

    val timestampFormat: Schema by lazy { getSchema(name = "TimestampFormat") }
    val timestamp: Schema by lazy { getSchema(name = "Timestamp", dependencies = setOf(timestampFormat)) }
    val month: Schema by lazy { getSchema(name = "Month") }
    val monthAndYear: Schema by lazy { getSchema(name = "MonthAndYear", dependencies = setOf(month)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(timestampFormat, timestamp, month, monthAndYear)
}