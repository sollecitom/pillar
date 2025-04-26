package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor.*
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccountAvroSerdeTests : AcmeAvroSerdeTestSpecification<Account>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Account.avroSerde

    override fun parameterizedArguments() = listOf(
        "internal-service-account" to ServiceAccount.Internal.create(),
        "external-service-account" to ServiceAccount.External.create(),
        "user-account" to UserAccount.create(),
    )
}