package sollecitom.libs.pillar.json.serialization.correlation.core.context

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.context.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.context.unauthenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import sollecitom.libs.swissknife.correlation.core.test.utils.customer.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*

@TestInstance(PER_CLASS)
class InvocationContextJsonSerializationTests : AcmeJsonSerdeTestSpecification<InvocationContext<*>>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = InvocationContext.jsonSerde

    override fun parameterizedArguments() = listOf(
        "authenticated" to InvocationContext.authenticated(),
        "unauthenticated" to InvocationContext.unauthenticated(),
        "authenticated-with-specified-tenant" to InvocationContext.authenticated(specifiedTargetTenant = { Tenant.create() }),
        "unauthenticated-with-specified-tenant" to InvocationContext.unauthenticated(specifiedTargetTenant = { Tenant.create() }),
        "authenticated-with-specified-locale" to InvocationContext.authenticated(specifiedLocale = { Locale.US }),
        "unauthenticated-with-specified-locale" to InvocationContext.unauthenticated(specifiedLocale = { Locale.GERMANY }),
        "authenticated-with-specified-target-customer" to InvocationContext.authenticated(specifiedTargetCustomer = { Customer.create() }),
        "unauthenticated-with-specified-target-customer" to InvocationContext.unauthenticated(specifiedTargetCustomer = { Customer.create(isTest = true) })
    )
}