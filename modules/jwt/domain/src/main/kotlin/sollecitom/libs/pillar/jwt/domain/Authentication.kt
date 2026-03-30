package sollecitom.libs.pillar.jwt.domain

import kotlin.time.Instant

/** Records when and how securely a user was authenticated, as extracted from a JWT. */
data class Authentication(val timestamp: Instant, val isSecure: Boolean) {

    companion object
}