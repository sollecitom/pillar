package sollecitom.libs.pillar.avro.serialization.core.locale

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object LocaleAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.locale") {

    val locale: Schema by lazy { getSchema(name = "Locale") }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(locale)
}