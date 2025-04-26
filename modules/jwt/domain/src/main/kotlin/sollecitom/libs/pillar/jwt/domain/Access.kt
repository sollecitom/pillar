package sollecitom.libs.pillar.jwt.domain

data class Access(val applicationRoles: Set<String>, val additionalRealmRoles: Set<String>, val keycloakAccountRoles: Set<String>) {

    val realmRoles: Set<String> get() = applicationRoles + additionalRealmRoles

    companion object
}