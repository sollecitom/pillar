package sollecitom.libs.pillar.jwt.domain

import kotlinx.datetime.Instant

data class Token(val id: String, val issuedAt: Instant, val expiresAt: Instant?) {

    companion object
}