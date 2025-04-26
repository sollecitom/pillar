package sollecitom.libs.pillar.avro.serialization.core.locale

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*

@TestInstance(PER_CLASS)
private class LocaleAvroSerdeTests : AcmeAvroSerdeTestSpecification<Locale>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val avroSerde get() = localeAvroSerde

    override fun parameterizedArguments(): List<Pair<String, Locale>> = Locale.availableLocales().map { "Name: ${it.displayName}" to it }.toList()
}