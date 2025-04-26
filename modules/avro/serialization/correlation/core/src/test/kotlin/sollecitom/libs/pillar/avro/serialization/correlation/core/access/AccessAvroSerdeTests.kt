package sollecitom.libs.pillar.avro.serialization.correlation.core.access

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.test.utils.access.authenticated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.unauthenticated
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessAvroSerdeTests : AcmeAvroSerdeTestSpecification<Access>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Access.avroSerde

    override fun parameterizedArguments() = listOf(
        "unauthenticated" to Access.unauthenticated(isTest = false),
        "unauthenticated-test" to Access.unauthenticated(isTest = true),
        "authenticated" to Access.authenticated()
    )
}