package sollecitom.libs.pillar.json.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ActorOnBehalf
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.DirectActor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.ImpersonatingActor
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.onBehalfOf
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.create
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.impersonating
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class DirectActorJsonSerializationTests : AcmeJsonSerdeTestSpecification<DirectActor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = DirectActor.jsonSerde

    override fun parameterizedArguments() = listOf("direct" to Actor.direct())
}

@TestInstance(PER_CLASS)
class ActorOnBehalfJsonSerializationTests : AcmeJsonSerdeTestSpecification<ActorOnBehalf>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ActorOnBehalf.jsonSerde

    override fun parameterizedArguments() = listOf("on-behalf" to Actor.direct().onBehalfOf(Actor.UserAccount.create()))
}

@TestInstance(PER_CLASS)
class ImpersonatingActorJsonSerializationTests : AcmeJsonSerdeTestSpecification<ImpersonatingActor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = ImpersonatingActor.jsonSerde

    override fun parameterizedArguments() = listOf("impersonating" to Actor.impersonating())
}
