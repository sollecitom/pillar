package sollecitom.libs.pillar.json.serialization.core.file

import sollecitom.libs.pillar.json.serialization.test.utils.AcmeJsonSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.file.FileContent
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URI

@TestInstance(PER_CLASS)
class FileContentJsonSerializationTests : AcmeJsonSerdeTestSpecification<FileContent>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = FileContent.jsonSerde

    override fun parameterizedArguments() = listOf(
        "inline" to FileContent.Inline("123".toByteArray(), FileContent.Format.CSV),
        "remote" to FileContent.Remote(1, URI.create("https://example.com/file.csv"), FileContent.Format.CSV),
    )
}