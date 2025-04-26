package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.impersonating
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.onBehalfOf
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ActorJsonSerializationTests : AcmeJsonSerdeTestSpecification<Actor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Actor.jsonSerde

    override fun parameterizedArguments() = listOf(
        "direct" to Actor.direct(),
        "on-behalf" to Actor.direct().onBehalfOf(Actor.UserAccount.create()),
        "impersonating" to Actor.direct().impersonating(Actor.UserAccount.create())
    )
}