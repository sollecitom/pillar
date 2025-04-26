package sollecitom.libs.pillar.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.pillar.jwt.test.utils.createParameters
import sollecitom.libs.pillar.jwt.test.utils.keycloakDev
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.jwt.domain.RSA.Variant.RSA_256
import sollecitom.libs.swissknife.jwt.jose4j.processor.newJwtProcessor
import sollecitom.libs.swissknife.jwt.test.utils.newRandomRSAJwtIssuer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AcmeJwtTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `generating and parsing Acme JWTs`() {

        val issuerKeyId = "issuer key"
        val keyCloakIssuer = Issuer.keycloakDev
        val issuer = newRandomRSAJwtIssuer(variant = RSA_256, keyId = issuerKeyId, id = keyCloakIssuer.id)
        val processor = newJwtProcessor(issuer)
        val parameters = AcmeJwtScheme.createParameters(
            issuer = keyCloakIssuer
        )
        val claims = parameters.asClaims()

        val jwt = issuer.issueJwt(claims)
        val parsedJwt = processor.readAndVerify(jwt)
        val parsedParameters = AcmeJwtScheme.parseParametersFromClaims(parsedJwt.claimsAsJson)

        assertThat(parsedParameters).isEqualTo(parameters)
    }
}