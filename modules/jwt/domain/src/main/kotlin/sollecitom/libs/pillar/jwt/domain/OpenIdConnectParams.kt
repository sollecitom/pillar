package sollecitom.libs.pillar.jwt.domain

/** OpenID Connect parameters from a JWT, including the requested scopes (e.g., "openid", "email", "profile"). */
data class OpenIdConnectParams(val requestedScope: Set<String>) {

    companion object
}