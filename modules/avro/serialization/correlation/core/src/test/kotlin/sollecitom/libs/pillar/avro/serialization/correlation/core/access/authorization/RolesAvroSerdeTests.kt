package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Roles
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class RolesAvroSerdeTests : AcmeAvroSerdeTestSpecification<Roles>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Roles.avroSerde

    override fun parameterizedArguments() = listOf(
        "single-value" to Roles(values = setOf(Role(name = Name.random()))),
        "multiple_values" to Roles(values = setOf(Role(name = Name.random()), Role(name = Name.random())))
    )
}