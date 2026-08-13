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
private class XtsEncryptionMetadataAvroSerdeTests : AcmeAvroSerdeTestSpecification<EncryptionMode.XTS.Metadata>, CoreDataGenerator by CoreDataGenerator.testProvider, CryptographicKeyGenerator {

    override val cryptographicOperations: CryptographicOperations = CryptographicOperations.bouncyCastle(secureRandom)
    override val avroSerde = EncryptionMode.XTS.Metadata.avroSerde

    override fun parameterizedArguments() = listOf(
        "XTS_AES_256" to newAesKey(variant = AES.Variant.AES_256_XTS).xts.encrypt("a message longer than one block".toByteArray(), dataUnitNumber = 42).metadata,
        "XTS_AES_128" to newAesKey(variant = AES.Variant.AES_128_XTS).xts.encrypt("a message longer than one block".toByteArray(), dataUnitNumber = 0).metadata
    )
}
