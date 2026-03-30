package sollecitom.libs.pillar.jwt.domain

/** Represents the access permissions extracted from a JWT, split by role source. */
data class Access(val applicationRoles: Set<String>, val additionalRealmRoles: Set<String>, val keycloakAccountRoles: Set<String>) {

    /** All realm-level roles, combining application-defined roles with additional Keycloak realm roles. */
    val realmRoles: Set<String> get() = applicationRoles + additionalRealmRoles

    companion object
}