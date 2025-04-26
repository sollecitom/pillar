package sollecitom.libs.pillar.jwt.domain

import kotlinx.datetime.Instant

data class Authentication(val timestamp: Instant, val isSecure: Boolean) {

    companion object
}