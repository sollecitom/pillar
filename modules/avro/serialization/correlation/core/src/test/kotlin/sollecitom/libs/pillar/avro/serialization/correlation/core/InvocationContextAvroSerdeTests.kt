package sollecitom.libs.pillar.avro.serialization.correlation.core

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.unauthenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*

@TestInstance(PER_CLASS)
private class InvocationContextAvroSerdeTests : AcmeAvroSerdeTestSpecification<InvocationContext<*>>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = InvocationContext.avroSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to InvocationContext.authenticated(),
        "unauthenticated" to InvocationContext.unauthenticated(),
        "authenticated-specified-locale" to InvocationContext.authenticated(specifiedLocale = { Locale.US }),
        "authenticated-specified-target-tenant" to InvocationContext.authenticated(specifiedTargetTenant = { Tenant.create() }),
        "unauthenticated-specified-locale" to InvocationContext.unauthenticated(specifiedLocale = { Locale.US }),
        "unauthenticated-specified-target-tenant" to InvocationContext.unauthenticated(specifiedTargetTenant = { Tenant.create() })
    )
}