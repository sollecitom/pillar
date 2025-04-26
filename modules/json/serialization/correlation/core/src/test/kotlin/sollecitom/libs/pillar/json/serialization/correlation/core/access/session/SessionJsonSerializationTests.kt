package sollecitom.libs.pillar.json.serialization.correlation.core.access.session

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.session.Session
import sollecitom.libs.swissknife.correlation.core.test.utils.access.session.federated
import sollecitom.libs.swissknife.correlation.core.test.utils.access.session.simple
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class SessionJsonSerializationTests : AcmeJsonSerdeTestSpecification<Session>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Session.jsonSerde

    override fun parameterizedArguments() = listOf(
        "simple" to Session.simple(),
        "federated" to Session.federated(),
    )
}