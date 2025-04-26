package sollecitom.libs.pillar.acme.business.domain

object AcmeRoles {

    const val customer = "customer"
    const val expert = "expert"
    const val admin = "admin"

    val values = setOf(customer, expert, admin)

    operator fun contains(role: String): Boolean = role in values
}

