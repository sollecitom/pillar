package sollecitom.libs.pillar.avro.serialization.core.identity

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

/** Avro schema catalogue for identity types (IdType enum and Id record). */
object IdentityAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.identity") {

    val idType : Schema by lazy { getSchema(name = "IdType") }
    val id: Schema by lazy { getSchema(name = "Id", dependencies = setOf(idType)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(id)
}