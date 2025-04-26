package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.core.identity.avroSchema
import sollecitom.libs.pillar.avro.serialization.core.locale.LocaleAvroSchemas
import sollecitom.libs.pillar.avro.serialization.correlation.core.access.authentication.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.customer.avroSchema
import sollecitom.libs.pillar.avro.serialization.correlation.core.tenant.avroSchema
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaCatalogueTemplate
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.authentication.Authentication
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.apache.avro.Schema

object ActorAvroSchemas : AvroSchemaCatalogueTemplate("acme.common.correlation.access.actor") {

    val userAccount by lazy { getSchema(name = "UserAccount", dependencies = setOf(Id.avroSchema, LocaleAvroSchemas.locale, Customer.avroSchema, Tenant.avroSchema)) }
    val internalServiceAccount by lazy { getSchema(name = "InternalServiceAccount", dependencies = setOf(Id.avroSchema)) }
    val externalServiceAccount by lazy { getSchema(name = "ExternalServiceAccount", dependencies = setOf(Id.avroSchema, Customer.avroSchema, Tenant.avroSchema)) }
    val serviceAccount by lazy { getSchema(name = "ServiceAccount", dependencies = setOf(internalServiceAccount, externalServiceAccount)) }
    val account by lazy { getSchema(name = "Account", dependencies = setOf(userAccount, serviceAccount)) }

    val directActor by lazy { getSchema(name = "DirectActor", dependencies = setOf(account, Authentication.avroSchema)) }
    val actorOnBehalf by lazy { getSchema(name = "ActorOnBehalf", dependencies = setOf(account, Authentication.avroSchema)) }
    val impersonatingActor by lazy { getSchema(name = "ImpersonatingActor", dependencies = setOf(account, Authentication.avroSchema)) }
    val actor by lazy { getSchema(name = "Actor", dependencies = setOf(directActor, actorOnBehalf, impersonatingActor)) }

    override val nestedContainers: Set<AvroSchemaContainer> = emptySet()

    override val all: Sequence<Schema> = sequenceOf(userAccount, internalServiceAccount, externalServiceAccount, serviceAccount, account, directActor, actorOnBehalf, impersonatingActor, actor)
}