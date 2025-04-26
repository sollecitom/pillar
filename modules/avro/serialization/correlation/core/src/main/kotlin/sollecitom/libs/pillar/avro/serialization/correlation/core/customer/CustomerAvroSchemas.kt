package sollecitom.libs.pillar.avro.serialization.correlation.core.customer

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import org.apache.avro.Schema

object CustomerAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.customer") {

    val customer by lazy { getSchema(name = "Customer", dependencies = setOf(Id.avroSchema)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(customer)
}