package sollecitom.libs.pillar.avro.serialization.ddd.event

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.pillar.avro.serialization.core.time.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import kotlinx.datetime.Instant
import org.apache.avro.Schema

object EventAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.event") {

    val happeningType by lazy { getSchema(name = "HappeningType") }
    val eventReference by lazy { getSchema(name = "EventReference", dependencies = setOf(Id.avroSchema, happeningType, Instant.avroSchema)) }
    val eventContext by lazy { getSchema(name = "EventContext", dependencies = setOf(eventReference, InvocationContext.avroSchema)) }
    val eventMetadata by lazy { getSchema(name = "EventMetadata", dependencies = setOf(Id.avroSchema, Instant.avroSchema, eventContext)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(happeningType, eventReference, eventContext, eventMetadata)
}