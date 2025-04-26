package sollecitom.libs.pillar.jwt.domain

data class User(val id: String, val organization: Organization, val userName: String, val emailAddress: String, val firstName: String, val lastName: String, val otherNames: List<String>) {

    val fullName: String = "$firstName${otherNames.takeUnless(List<String>::isEmpty)?.joinToString(separator = " ") ?: ""} $lastName"

    companion object
}