package sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.avro.serialization.core.identity.IdentityAvroSchemas
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.apache.avro.Schema

object AccessScopeAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.scope") {

    val container by lazy { getSchema(name = "AccessContainer", dependencies = setOf(IdentityAvroSchemas.id)) }
    val scope by lazy { getSchema(name = "AccessScope", dependencies = setOf(container)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(scope, container)
}