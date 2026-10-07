package sollecitom.libs.pillar.avro.serialization.core.locale

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import org.apache.avro.Schema

/** Avro schema catalogue for locale types. */
object LocaleAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.locale") {

    val locale: Schema by lazy { getSchema(name = "Locale") }

    override val all: Sequence<Schema> get() = sequenceOf(locale)
}