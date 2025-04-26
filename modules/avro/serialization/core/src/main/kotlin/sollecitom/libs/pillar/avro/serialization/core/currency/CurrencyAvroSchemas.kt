package sollecitom.libs.pillar.avro.serialization.core.currency

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object CurrencyAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.currency") {

    val currency : Schema by lazy { getSchema(name = "Currency") }
    val currencyAmount : Schema by lazy { getSchema(name = "CurrencyAmount", dependencies = setOf(currency)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(currency, currencyAmount)
}