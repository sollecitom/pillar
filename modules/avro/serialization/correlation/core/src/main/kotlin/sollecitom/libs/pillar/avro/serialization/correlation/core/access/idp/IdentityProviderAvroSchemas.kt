package sollecitom.libs.pillar.avro.serialization.correlation.core.access.idp

import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.Schema

object IdentityProviderAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.idp") {

    val identityProvider by lazy { getSchema(name = "IdentityProvider", dependencies = setOf(Customer.avroSchema, Tenant.avroSchema)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(identityProvider)
}