package sollecitom.libs.pillar.jwt.domain

/** The company or tenant a user belongs to, as identified in the JWT claims. */
data class Organization(val id: String, val name: String) {

    companion object
}