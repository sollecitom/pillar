package sollecitom.libs.pillar.protected_value.messaging.serialization.avro

import sollecitom.libs.pillar.protected_value.domain.ProtectedString
import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.key.generator.CryptographicKeyGenerator
import sollecitom.libs.swissknife.cryptography.domain.key.generator.newAesKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.bouncyCastle
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValueFactory
import sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle.aes256WithCTR
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ProtectedStringAvroSerdeTests : AcmeAvroSerdeTestSpecification<ProtectedString>, CoreDataGenerator by CoreDataGenerator.testProvider, CryptographicKeyGenerator {

    override val cryptographicOperations: CryptographicOperations = CryptographicOperations.bouncyCastle(secureRandom)
    private val key = newAesKey(variant = AES_256)
    private val factory = ProtectedValueFactory.aes256WithCTR { key }
    override val avroSerde = ProtectedValue.stringAvroSerde

    override fun parameterizedArguments() = listOf(
        "AES_256" to runBlocking { factory.protectValue("hello world", "message".let(::Name), newId.external(), String::toByteArray) }
    )
}