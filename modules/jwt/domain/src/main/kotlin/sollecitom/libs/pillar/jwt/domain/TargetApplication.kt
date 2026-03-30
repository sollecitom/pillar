package sollecitom.libs.pillar.jwt.domain

/** The application a JWT was issued for, including the intended [audience], the [authorizedParty] (OpenID Connect `azp`), and CORS [allowedOrigins]. */
data class TargetApplication(val audience: String, val authorizedParty: String, val allowedOrigins: Set<String>) {

    companion object
}