package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.pillar.avro.serialization.core.time.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.session.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.session.FederatedSession
import sollecitom.libs.swissknife.correlation.core.domain.access.session.SimpleSession
import kotlinx.datetime.Instant
import org.apache.avro.Schema

object AuthenticationAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.authentication") {

    val authenticationToken by lazy { getSchema(name = "AuthenticationToken", dependencies = setOf(Id.avroSchema, Instant.avroSchema)) }
    val statelessAuthentication by lazy { getSchema(name = "StatelessAuthentication", dependencies = setOf(authenticationToken)) }
    val credentialsBasedAuthentication by lazy { getSchema(name = "CredentialsBasedAuthentication", dependencies = setOf(authenticationToken, SimpleSession.avroSchema)) }
    val federatedAuthentication by lazy { getSchema(name = "FederatedAuthentication", dependencies = setOf(authenticationToken, FederatedSession.avroSchema)) }
    val authentication by lazy { getSchema(name = "Authentication", dependencies = setOf(statelessAuthentication, credentialsBasedAuthentication, federatedAuthentication)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(authenticationToken, statelessAuthentication, credentialsBasedAuthentication, federatedAuthentication, authentication)
}