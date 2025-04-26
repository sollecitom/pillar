package sollecitom.libs.pillar.json.serialization.pagination

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.pagination.domain.Pagination
import sollecitom.libs.swissknife.pagination.test.utils.create
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class PaginationArgumentsJsonSerializationTests : AcmeJsonSerdeTestSpecification<Pagination.Arguments>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Pagination.Arguments.jsonSerde

    override fun parameterizedArguments() = listOf(
        "random" to Pagination.Arguments.create(),
        "without-continuation-token" to Pagination.Arguments.create(continuationToken = null),
    )
}