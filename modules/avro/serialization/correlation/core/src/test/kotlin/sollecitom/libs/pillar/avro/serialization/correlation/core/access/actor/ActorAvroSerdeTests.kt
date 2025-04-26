package sollecitom.libs.pillar.avro.serialization.correlation.core.access.actor

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.actor.Actor
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.direct
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.impersonating
import sollecitom.libs.swissknife.correlation.core.test.utils.access.actor.onBehalf
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ActorAvroSerdeTests : AcmeAvroSerdeTestSpecification<Actor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde = Actor.avroSerde

    override fun parameterizedArguments() = listOf(
        "direct" to Actor.direct(),
        "impersonating" to Actor.impersonating(),
        "on-behalf" to Actor.onBehalf()
    )
}