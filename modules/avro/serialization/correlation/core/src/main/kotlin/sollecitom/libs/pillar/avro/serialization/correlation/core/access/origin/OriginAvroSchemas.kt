package sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.client.info.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo
import org.apache.avro.Schema

object OriginAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.origin") {

    val origin by lazy { getSchema(name = "Origin", dependencies = setOf(ClientInfo.avroSchema)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(origin)
}