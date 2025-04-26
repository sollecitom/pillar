package sollecitom.libs.pillar.avro.serialization.correlation.core.access.scope

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessContainer
import sollecitom.libs.swissknife.correlation.core.domain.access.scope.AccessScope
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessScopeAvroSerdeTests : AcmeAvroSerdeTestSpecification<AccessScope>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = AccessScope.avroSerde

    override fun parameterizedArguments() = listOf(
        "single-value" to AccessScope(containerStack = listOf(AccessContainer(id = newId.external()))),
        "multiple-values" to AccessScope(containerStack = listOf(AccessContainer(id = newId.external()), AccessContainer(id = newId.external()), AccessContainer(id = newId.external())))
    )
}