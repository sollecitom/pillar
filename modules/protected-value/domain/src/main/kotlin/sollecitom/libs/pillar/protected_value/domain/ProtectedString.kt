package sollecitom.libs.pillar.protected_value.domain

import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue

typealias ProtectedString = ProtectedValue<String, EncryptionMode.CTR.Metadata>