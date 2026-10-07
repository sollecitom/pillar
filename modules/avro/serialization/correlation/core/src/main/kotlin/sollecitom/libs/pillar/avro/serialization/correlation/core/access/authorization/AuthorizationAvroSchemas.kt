package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.avro.serialization.core.identity.IdentityAvroSchemas
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import org.apache.avro.Schema

object AuthorizationAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.authorization") {

    val role by lazy { getSchema(name = "Role", dependencies = setOf(IdentityAvroSchemas.id)) }
    val roles by lazy { getSchema(name = "Roles", dependencies = setOf(role)) }
    val authorizationPrincipal by lazy { getSchema(name = "AuthorizationPrincipal", dependencies = setOf(roles)) }

    override val all: Sequence<Schema> get() = sequenceOf(role, roles, authorizationPrincipal)
}