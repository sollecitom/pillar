package sollecitom.libs.pillar.jwt.domain

import sollecitom.libs.swissknife.jwt.domain.StringOrURI

/** The identity provider that issued a JWT (e.g., a Keycloak realm URL). */
data class Issuer(val id: StringOrURI) {

    companion object
}