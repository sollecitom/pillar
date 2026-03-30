package sollecitom.libs.pillar.protected_value.domain

import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue

/** A string value protected by symmetric CTR-mode encryption. Use this for sensitive data that must be encrypted at rest. */
typealias ProtectedString = ProtectedValue<String, EncryptionMode.CTR.Metadata>