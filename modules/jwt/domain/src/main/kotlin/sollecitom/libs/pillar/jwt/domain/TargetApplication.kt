package sollecitom.libs.pillar.jwt.domain

data class TargetApplication(val audience: String, val authorizedParty: String, val allowedOrigins: Set<String>) {

    companion object
}