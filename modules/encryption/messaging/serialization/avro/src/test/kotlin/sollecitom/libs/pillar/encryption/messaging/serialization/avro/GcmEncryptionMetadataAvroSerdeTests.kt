package sollecitom.libs.pillar.encryption.messaging.serialization.avro

import sollecitom.libs.pillar.avro.serialization.test.utils.AcmeAvroSerdeTestSpecification
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.key.generator.CryptographicKeyGenerator
import sollecitom.libs.swissknife.cryptography.domain.key.generator.newAesKey
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.bouncyCastle
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class GcmEncryptionMetadataAvroSerdeTests : AcmeAvroSerdeTestSpecification<EncryptionMode.GCM.Metadata>, CoreDataGenerator by CoreDataGenerator.testProvider, CryptographicKeyGenerator {

    override val cryptographicOperations: CryptographicOperations = CryptographicOperations.bouncyCastle(secureRandom)
    override val avroSerde = EncryptionMode.GCM.Metadata.avroSerde

    override fun parameterizedArguments() = listOf(
        "AES_256_without_associated_data" to newAesKey(variant = AES.Variant.AES_256).gcm.encryptWithRandomIV("hello".toByteArray()).metadata,
        "AES_256_with_associated_data" to newAesKey(variant = AES.Variant.AES_256).gcm.encryptWithRandomIV("hello".toByteArray(), associatedData = "a public header".toByteArray()).metadata
    )
}
