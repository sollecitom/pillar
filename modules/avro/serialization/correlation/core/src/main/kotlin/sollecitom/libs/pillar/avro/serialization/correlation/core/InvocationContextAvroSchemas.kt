package sollecitom.libs.pillar.avro.serialization.correlation.core

import sollecitom.libs.pillar.avro.serialization.core.locale.LocaleAvroSchemas
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.toggles.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.trace.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import org.apache.avro.Schema

object InvocationContextAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.context") {

    val invocationContext by lazy { getSchema(name = "InvocationContext", dependencies = setOf(Access.avroSchema, Trace.avroSchema, Toggles.avroSchema, LocaleAvroSchemas.locale, Customer.avroSchema, Tenant.avroSchema)) }

    // TODO add all nested containers
    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(invocationContext)
}