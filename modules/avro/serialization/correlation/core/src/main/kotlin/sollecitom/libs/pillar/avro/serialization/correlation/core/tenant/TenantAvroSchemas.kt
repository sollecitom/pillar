package sollecitom.libs.pillar.avro.serialization.correlation.core.tenant

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import org.apache.avro.Schema

object TenantAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.tenant") {

    val tenant by lazy { getSchema(name = "Tenant", dependencies = setOf(Id.avroSchema)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(tenant)
}