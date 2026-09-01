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

/** Round-trips the mode-agnostic envelope, so the branch discriminator stays in step with the schema's union. */
@TestInstance(PER_CLASS)
private class EncryptionMetadataAvroSerdeTests : AcmeAvroSerdeTestSpecification<EncryptionMode.Metadata>, CoreDataGenerator by CoreDataGenerator.testProvider, CryptographicKeyGenerator {

    override val cryptographicOperations: CryptographicOperations = CryptographicOperations.bouncyCastle(secureRandom)
    override val avroSerde = EncryptionMode.Metadata.avroSerde

    override fun parameterizedArguments(): List<Pair<String, EncryptionMode.Metadata>> = listOf(
        "GCM_without_associated_data" to newAesKey(variant = AES.Variant.AES_256).gcm.encryptWithRandomIV("hello".toByteArray()).metadata,
        "GCM_with_associated_data" to newAesKey(variant = AES.Variant.AES_256).gcm.encryptWithRandomIV("hello".toByteArray(), associatedData = "a public header".toByteArray()).metadata
    )
}
