package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.origin.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.AuthorizationPrincipal
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import org.apache.avro.Schema

object AccessAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access") {

    val unauthenticatedAccess by lazy { getSchema(name = "UnauthenticatedAccess", dependencies = setOf(Origin.avroSchema, AuthorizationPrincipal.avroSchema, AccessScope.avroSchema)) }
    val authenticatedAccess by lazy { getSchema(name = "AuthenticatedAccess", dependencies = setOf(Actor.avroSchema, Origin.avroSchema, AuthorizationPrincipal.avroSchema, AccessScope.avroSchema)) }
    val access by lazy { getSchema(name = "Access", dependencies = setOf(unauthenticatedAccess, authenticatedAccess)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(unauthenticatedAccess)
}