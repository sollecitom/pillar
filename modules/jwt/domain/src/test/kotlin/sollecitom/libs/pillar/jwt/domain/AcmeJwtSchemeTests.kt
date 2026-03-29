package sollecitom.libs.pillar.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class AcmeJwtSchemeTests {

    private val user = User(
        id = "user-123",
        organization = Organization(id = "org-456", name = "acme"),
        userName = "jdoe",
        emailAddress = "jdoe@acme.com",
        firstName = "John",
        lastName = "Doe",
        otherNames = emptyList()
    )
    private val access = Access(
        applicationRoles = setOf("customer"),
        additionalRealmRoles = setOf("offline_access", "uma_authorization"),
        keycloakAccountRoles = setOf("manage-account", "view-profile")
    )
    private val authentication = Authentication(
        timestamp = Instant.fromEpochSeconds(1700000000),
        isSecure = true
    )
    private val token = Token(
        id = "token-789",
        issuedAt = Instant.fromEpochSeconds(1700000100),
        expiresAt = Instant.fromEpochSeconds(1700003700)
    )
    private val session = Session(id = "session-1", stateId = "session-state-1")
    private val targetApplication = TargetApplication(
        audience = "account",
        authorizedParty = "example.web",
        allowedOrigins = setOf("https://app.example.dev.acme.info/*")
    )
    private val issuer = Issuer(id = StringOrURI("https://keycloak.tools.acme.info/realms/acme"))
    private val openIdConnectParams = OpenIdConnectParams(requestedScope = setOf("openid", "email", "profile"))

    @Nested
    @TestInstance(PER_CLASS)
    inner class `claims roundtrip` {

        @Test
        fun `serializing and deserializing parameters preserves all fields`() {

            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = access,
                isUserEmailAddressVerified = true,
                authentication = authentication,
                token = token,
                session = session,
                targetApplication = targetApplication,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }

        @Test
        fun `with unverified email address`() {

            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = access,
                isUserEmailAddressVerified = false,
                authentication = authentication,
                token = token,
                session = session,
                targetApplication = targetApplication,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }

        @Test
        fun `with insecure authentication`() {

            val insecureAuth = Authentication(timestamp = Instant.fromEpochSeconds(1700000000), isSecure = false)
            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = access,
                isUserEmailAddressVerified = true,
                authentication = insecureAuth,
                token = token,
                session = session,
                targetApplication = targetApplication,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }

        @Test
        fun `with null token expiry`() {

            val tokenWithoutExpiry = Token(id = "token-789", issuedAt = Instant.fromEpochSeconds(1700000100), expiresAt = null)
            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = access,
                isUserEmailAddressVerified = true,
                authentication = authentication,
                token = tokenWithoutExpiry,
                session = session,
                targetApplication = targetApplication,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }

        @Test
        fun `with multiple allowed origins`() {

            val multiOriginApp = TargetApplication(
                audience = "account",
                authorizedParty = "example.web",
                allowedOrigins = setOf("https://app.example.dev.acme.info/*", "https://app.example.dev.acme.info")
            )
            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = access,
                isUserEmailAddressVerified = true,
                authentication = authentication,
                token = token,
                session = session,
                targetApplication = multiOriginApp,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }

        @Test
        fun `with empty application roles`() {

            val noAppRolesAccess = Access(
                applicationRoles = emptySet(),
                additionalRealmRoles = setOf("offline_access"),
                keycloakAccountRoles = setOf("view-profile")
            )
            val parameters = AcmeJwtScheme.Parameters(
                user = user,
                access = noAppRolesAccess,
                isUserEmailAddressVerified = true,
                authentication = authentication,
                token = token,
                session = session,
                targetApplication = targetApplication,
                issuer = issuer,
                openIdConnectParams = openIdConnectParams,
                authorizationHeaderType = "Bearer"
            )

            val claims = parameters.asClaims()
            val parsed = AcmeJwtScheme.parseParametersFromClaims(claims)

            assertThat(parsed).isEqualTo(parameters)
        }
    }
}
