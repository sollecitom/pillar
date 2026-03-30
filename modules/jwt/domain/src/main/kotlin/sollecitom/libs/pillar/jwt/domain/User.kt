package sollecitom.libs.pillar.jwt.domain

/** The authenticated user as extracted from JWT claims. Belongs to an [organization] and may have multiple names (e.g., "Jean Pierre Dupont"). */
data class User(val id: String, val organization: Organization, val userName: String, val emailAddress: String, val firstName: String, val lastName: String, val otherNames: List<String>) {

    /** Full name assembled from first, other, and last names (e.g., "Jean Pierre Dupont"). */
    val fullName: String = "$firstName${otherNames.takeUnless(List<String>::isEmpty)?.joinToString(separator = " ") ?: ""} $lastName"

    companion object
}