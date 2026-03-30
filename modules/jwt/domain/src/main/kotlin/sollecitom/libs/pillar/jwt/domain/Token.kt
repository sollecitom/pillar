package sollecitom.libs.pillar.jwt.domain

import kotlin.time.Instant

/** The JWT token's own metadata: its unique [id], when it was [issuedAt], and optional [expiresAt]. */
data class Token(val id: String, val issuedAt: Instant, val expiresAt: Instant?) {

    companion object
}