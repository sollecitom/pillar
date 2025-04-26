package sollecitom.libs.pillar.avro.serialization.correlation.core.access.authorization

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.authorization.Role
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class RoleAvroSerdeTests : AcmeAvroSerdeTestSpecification<Role>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Role.avroSerde

    override fun parameterizedArguments() = listOf(
        "test-name" to Role("some-role".let(::Name))
    )
}