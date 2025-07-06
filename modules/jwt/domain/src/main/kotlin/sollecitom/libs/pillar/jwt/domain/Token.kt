package sollecitom.libs.pillar.jwt.domain

import kotlin.time.Instant

data class Token(val id: String, val issuedAt: Instant, val expiresAt: Instant?) {

    companion object
}