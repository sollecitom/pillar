package sollecitom.libs.pillar.jwt.domain

/** Represents the Keycloak session associated with a JWT. The [stateId] is used for OpenID Connect Session Management. */
data class Session(val id: String, val stateId: String) {

    companion object
}