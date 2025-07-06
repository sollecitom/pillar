package sollecitom.libs.pillar.jwt.domain

import kotlin.time.Instant

data class Authentication(val timestamp: Instant, val isSecure: Boolean) {

    companion object
}