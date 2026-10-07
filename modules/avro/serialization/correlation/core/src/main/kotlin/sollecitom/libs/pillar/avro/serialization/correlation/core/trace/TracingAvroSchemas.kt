package sollecitom.libs.pillar.avro.serialization.correlation.core.trace

import sollecitom.libs.pillar.avro.serialization.core.identity.IdentityAvroSchemas
import sollecitom.libs.pillar.avro.serialization.core.time.TimeAvroSchemas
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import org.apache.avro.Schema

object TracingAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.trace") {

    val externalInvocationTrace by lazy { getSchema(name = "ExternalInvocationTrace", dependencies = setOf(IdentityAvroSchemas.id)) }
    val invocationTrace by lazy { getSchema(name = "InvocationTrace", dependencies = setOf(IdentityAvroSchemas.id, TimeAvroSchemas.timestamp)) }
    val trace by lazy { getSchema(name = "Trace", dependencies = setOf(invocationTrace, externalInvocationTrace)) }

    override val all: Sequence<Schema> get() = sequenceOf(externalInvocationTrace, invocationTrace, trace)
}