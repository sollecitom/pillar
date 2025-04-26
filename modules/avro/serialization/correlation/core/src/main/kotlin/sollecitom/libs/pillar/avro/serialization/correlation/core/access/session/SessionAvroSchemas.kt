package sollecitom.libs.pillar.avro.serialization.correlation.core.access.session

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.idp.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import org.apache.avro.Schema

object SessionAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.session") {

    val simpleSession by lazy { getSchema(name = "SimpleSession", dependencies = setOf(Id.avroSchema)) }
    val federatedSession by lazy { getSchema(name = "FederatedSession", dependencies = setOf(Id.avroSchema, IdentityProvider.avroSchema)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(simpleSession, federatedSession)
}