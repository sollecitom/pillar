package sollecitom.libs.pillar.jwt.domain

import sollecitom.libs.pillar.acme.business.domain.AcmeRoles
import sollecitom.libs.swissknife.json.utils.*
import sollecitom.libs.swissknife.jwt.domain.StringOrURI
import sollecitom.libs.swissknife.kotlin.extensions.text.removeFromLast
import kotlinx.datetime.Instant
import org.json.JSONArray
import org.json.JSONObject

fun AcmeJwtScheme.Parameters.asClaims() = AcmeJwtScheme.claimsFor(parameters = this)

object AcmeJwtScheme {

    data class Parameters(
        val user: User,
        val access: Access,
        val isUserEmailAddressVerified: Boolean,
        val authentication: Authentication,
        val token: Token,
        val session: Session,
        val targetApplication: TargetApplication,
        val issuer: Issuer,
        val openIdConnectParams: OpenIdConnectParams,
        val authorizationHeaderType: String
    )

    fun claimsFor(
        user: User,
        access: Access,
        isUserEmailAddressVerified: Boolean,
        authentication: Authentication,
        token: Token,
        session: Session,
        targetApplication: TargetApplication,
        issuer: Issuer,
        openIdConnectParams: OpenIdConnectParams,
        authorizationHeaderType: String
    ) = claimsFor(Parameters(user, access, isUserEmailAddressVerified, authentication, token, session, targetApplication, issuer, openIdConnectParams, authorizationHeaderType))

    fun parseParametersFromClaims(claims: JSONObject): Parameters {

        val user = claims.user()
        val access = claims.access()
        val isUserEmailAddressVerified = claims.isUserEmailAddressVerified()
        val authentication = claims.authentication()
        val token = claims.token()
        val session = claims.session()
        val targetApplication = claims.targetApplication()
        val issuer = claims.issuer()
        val openIdConnectParams = claims.openIdConnectParams()
        val authorizationHeaderType = claims.authorizationHeaderType()
        return Parameters(
            user = user,
            access = access,
            isUserEmailAddressVerified = isUserEmailAddressVerified,
            authentication = authentication,
            token = token,
            session = session,
            targetApplication = targetApplication,
            issuer = issuer,
            openIdConnectParams = openIdConnectParams,
            authorizationHeaderType = authorizationHeaderType
        )
    }

    private fun JSONObject.user(): User {

        val id = getRequiredString(Fields.SUBJECT)
        val orgId = getRequiredString(Fields.ORGANIZATION_ID)
        val emailAddress = getRequiredString(Fields.EMAIL_ADDRESS)
        val organizationName = emailAddress.split("@").drop(1).single().removeFromLast(".")
        val organization = Organization(id = orgId, name = organizationName)
        val userName = getRequiredString(Fields.USERNAME)
        val firstName = getRequiredString(Fields.GIVEN_NAME)
        val lastName = getRequiredString(Fields.FAMILY_NAME)
        val fullName = getRequiredString(Fields.FULL_NAME)
        val otherNames = fullName.removePrefix(firstName).removeSuffix(lastName).trim().split(" ").filterNot(String::isBlank)
        return User(id = id, organization = organization, userName = userName, emailAddress = emailAddress, firstName = firstName, lastName = lastName, otherNames = otherNames)
    }

    private fun JSONObject.access(): Access {

        val allRoles = getRequiredJSONObject(Fields.REALM_ACCESS).getRequiredStringArray(Fields.ROLES).toSet()
        val applicationRoles = allRoles.filter { it in AcmeRoles }.toSet()
        val additionalRealmRoles = allRoles - applicationRoles
        val keycloakAccount = getRequiredJSONObject(Fields.RESOURCE_ACCESS).getRequiredJSONObject(Fields.ACCOUNT_KEYCLOAK_RESOURCE)
        val keycloakAccountRoles = keycloakAccount.getRequiredStringArray(Fields.ROLES).toSet()
        return Access(applicationRoles = applicationRoles, additionalRealmRoles = additionalRealmRoles, keycloakAccountRoles = keycloakAccountRoles)
    }

    private fun JSONObject.isUserEmailAddressVerified(): Boolean = getRequiredBoolean(Fields.IS_USER_EMAIL_VERIFIED)

    private fun JSONObject.authentication(): Authentication {

        val timestamp = getRequiredLong(Fields.AUTHENTICATION_TIME).let(Instant.Companion::fromEpochSeconds)
        val isSecure = when (val acr = getRequiredString(Fields.AUTHENTICATION_CONTEXT_CLASS_REFERENCE)) {
            "1" -> true
            "0" -> false
            else -> error("Unsupported JWT ACR value '$acr'")
        }
        return Authentication(timestamp = timestamp, isSecure = isSecure)
    }

    private fun JSONObject.session(): Session {

        val id = getRequiredString(Fields.SESSION_ID)
        val stateId = getRequiredString(Fields.SESSION_STATE)
        return Session(id = id, stateId = stateId)
    }

    private fun JSONObject.token(): Token {

        val id = getRequiredString(Fields.JWT_ID)
        val issuedAt = getRequiredLong(Fields.ISSUED_AT).let(Instant.Companion::fromEpochSeconds)
        val expiresAt = getLongOrNull(Fields.EXPIRES_AT)?.let(Instant.Companion::fromEpochSeconds)
        return Token(id = id, issuedAt = issuedAt, expiresAt = expiresAt)
    }

    private fun JSONObject.targetApplication(): TargetApplication {

        val audience = getRequiredString(Fields.AUDIENCE)
        val authorizedParty = getRequiredString(Fields.AUTHORIZED_PARTY)
        val allowedOrigins = getRequiredStringArray(Fields.ALLOWED_ORIGINS).toSet()
        return TargetApplication(audience = audience, authorizedParty = authorizedParty, allowedOrigins = allowedOrigins)
    }

    private fun JSONObject.issuer(): Issuer {

        val id = getRequiredString(Fields.ISSUER).let(::StringOrURI)
        return Issuer(id = id)
    }

    private fun JSONObject.openIdConnectParams(): OpenIdConnectParams {

        val requestedScope = getRequiredString(Fields.OPEN_ID_CONNECT_SCOPE).split(" ").filterNot(String::isBlank).toSet()
        return OpenIdConnectParams(requestedScope = requestedScope)
    }

    private fun JSONObject.authorizationHeaderType(): String = getRequiredString(Fields.TYPE)

    fun claimsFor(
        parameters: Parameters
    ) = with(parameters) {
        keycloakClaims(
            subject = user.id,
            customerId = user.organization.id,
            username = user.userName,
            emailAddress = user.emailAddress,
            isUserEmailAddressVerified = isUserEmailAddressVerified,
            givenName = user.firstName,
            familyName = user.lastName,
            fullName = user.fullName,
            realmRoles = access.realmRoles.toList(),
            keycloakAccountRoles = access.keycloakAccountRoles.toList(),
            audience = targetApplication.audience,
            authorizedParty = targetApplication.authorizedParty,
            allowedOrigins = targetApplication.allowedOrigins.toList(),
            authenticationTime = authentication.timestamp,
            authenticationContextClassReference = authentication.contextClassReference,
            jwtId = token.id,
            issuedAt = token.issuedAt,
            expiresAt = token.expiresAt,
            sessionId = session.id,
            sessionState = session.stateId,
            issuer = issuer.id,
            type = authorizationHeaderType,
            openIdConnectScope = openIdConnectParams.requestedScope.toList()
        )
    }

    private fun keycloakClaims(
        subject: String,
        customerId: String,
        emailAddress: String,
        username: String,
        givenName: String,
        familyName: String,
        fullName: String,
        realmRoles: List<String>,
        keycloakAccountRoles: List<String>,
        audience: String,
        authorizedParty: String,
        openIdConnectScope: List<String>,
        sessionId: String,
        sessionState: String,
        jwtId: String,
        authenticationTime: Instant,
        issuedAt: Instant,
        expiresAt: Instant?,
        isUserEmailAddressVerified: Boolean,
        allowedOrigins: List<String>,
        issuer: StringOrURI,
        type: String,
        authenticationContextClassReference: String
    ): JSONObject {

        val json = JSONObject()
        json.put(Fields.SUBJECT, subject)
        json.put(Fields.ORGANIZATION_ID, customerId)
        json.put(Fields.EMAIL_ADDRESS, emailAddress)
        json.put(Fields.USERNAME, username)
        json.put(Fields.GIVEN_NAME, givenName)
        json.put(Fields.FAMILY_NAME, familyName)
        json.put(Fields.FULL_NAME, fullName)
        json.put(Fields.JWT_ID, jwtId)
        json.put(Fields.AUTHENTICATION_TIME, authenticationTime.epochSeconds)
        json.put(Fields.ISSUED_AT, issuedAt.epochSeconds)
        json.put(Fields.EXPIRES_AT, expiresAt?.epochSeconds)
        json.put(Fields.SESSION_ID, sessionId)
        json.put(Fields.SESSION_STATE, sessionState)
        json.put(Fields.AUTHORIZED_PARTY, authorizedParty)
        json.put(Fields.OPEN_ID_CONNECT_SCOPE, openIdConnectScope.joinToString(separator = " "))
        json.put(Fields.IS_USER_EMAIL_VERIFIED, isUserEmailAddressVerified)
        json.put(Fields.ALLOWED_ORIGINS, JSONArray().putAll(allowedOrigins))
        json.put(Fields.TYPE, type)
        json.put(Fields.ISSUER, issuer.value)
        json.put(Fields.AUTHENTICATION_CONTEXT_CLASS_REFERENCE, authenticationContextClassReference)
        json.put(Fields.AUDIENCE, audience)
        json.put(Fields.REALM_ACCESS, JSONObject().put(Fields.ROLES, JSONArray().putAll(realmRoles)))
        json.put(Fields.RESOURCE_ACCESS, JSONObject().put(Fields.ACCOUNT_KEYCLOAK_RESOURCE, JSONObject().put(Fields.ROLES, JSONArray().putAll(keycloakAccountRoles))))

        return json
    }

    private val Authentication.contextClassReference: String get() = if (isSecure) "1" else "0"

    private object Fields {
        const val SUBJECT = "sub" // the subject the token was issued to e.g. the user ID in our case
        const val ORGANIZATION_ID = "orgId" // the customer ID e.g. the ID of the company the user belongs to
        const val EMAIL_ADDRESS = "email" // the email of the user
        const val GIVEN_NAME = "given_name" // first name of the user
        const val FAMILY_NAME = "family_name" // last name of the user
        const val FULL_NAME = "name" // full name of the user; might be needed when a user has more than one first name (so full name contains is "${first_name} ${another_name} ${last_name}")
        const val USERNAME = "preferred_username"
        const val AUTHORIZED_PARTY = "azp" // essentially another field for audience ("aud") (used in OpenID Connect, optional) e.g., "example.web"
        const val OPEN_ID_CONNECT_SCOPE = "scope" // (used in OpenID Connect to request additional details about the user) e.g., "openid email profile"
        const val SESSION_ID = "sid" // the ID of the session (same ID if refresh tokens are used) e.g., "2eb5fde9-8acf-4a82-8d9b-b2cec05dd9ad"
        const val SESSION_STATE = "session_state" // (used in OpenID Connect Session Management) e.g., "2eb5fde9-8acf-4a82-8d9b-b2cec05dd9ad"
        const val JWT_ID = "jti" // the ID of the JWT e.g., "e1bc283a-5295-4003-9e30-5dc06f61dbd1"
        const val ISSUED_AT = "iat" // the instant the token was issued, as seconds from epoch
        const val EXPIRES_AT = "exp" // the instant the token expires, as seconds from epoch
        const val AUTHENTICATION_TIME = "auth_time" // the instant the user was authenticated (this is not always the same as the time the token was issued ("iat"), since tokens can be refreshed, etc.)
        const val IS_USER_EMAIL_VERIFIED = "email_verified" // whether the user's email address is verified e.g., true
        const val ALLOWED_ORIGINS = "allowed-origins" // (probably a custom attribute), the set of addresses that are allowed to use the token e.g., ["https://app.example.dev.acme.info/*", "https://app.example.dev.acme.info"]
        const val TYPE = "typ" // the type of authorization token e.g., "Bearer"
        const val ISSUER = "iss" // the address of the issuer of the JWT e.g., https://keycloak.tools.acme.info/realms/acme
        const val AUTHENTICATION_CONTEXT_CLASS_REFERENCE = "acr" // the strength of the authentication method. "0" means poor, the other values are arbitrary. "1" is what we use now.
        const val AUDIENCE = "aud" // it's the address of the application meant to use the token e.g., example.com; at the moment we put "account" in here, not sure why
        const val REALM_ACCESS = "realm_access" // specifies the access the user has in the KeyCloak realm - this contains the roles the user has in our application e.g., "customer", plus some roles KeyCloak adds "offline_access", "uma_authorization"
        const val ROLES = "roles" // specifies the roles the user has either as part of the "realm_access" or of the "resource_access" e.g., ["offline_access", "uma_authorization", "default-roles-apps", "customer"] for "realm_access"
        const val RESOURCE_ACCESS = "resource_access" // specifies the access the user has over the resources in KeyCloak e.g., viewing and changing their own account
        const val ACCOUNT_KEYCLOAK_RESOURCE = "account" // it's name of the user account resource in KeyCloak (it's the key for the "roles" field that specify the access the user has over his own KeyCloak account)
    }
}