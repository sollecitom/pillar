package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.externalService
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.internalService
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.user
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*

@TestInstance(PER_CLASS)
class ActorAccountJsonSerializationTests : AcmeJsonSerdeTestSpecification<Actor.Account>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Actor.Account.jsonSerde

    override fun parameterizedArguments() = listOf(
        "user" to Actor.Account.user(),
        "user-with-explicit-locale" to Actor.Account.user(locale = Locale.ITALY),
        "external-service" to Actor.Account.externalService(),
        "internal-service" to Actor.Account.internalService()
    )
}