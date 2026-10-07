package sollecitom.libs.pillar.avro.serialization.core.identity

import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import org.apache.avro.Schema

/** Avro schema catalogue for identity types (IdType enum and Id record). */
object IdentityAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.identity") {

    val idType : Schema by lazy { getSchema(name = "IdType") }
    val id: Schema by lazy { getSchema(name = "Id", dependencies = setOf(idType)) }

    override val all: Sequence<Schema> get() = sequenceOf(idType, id)
}