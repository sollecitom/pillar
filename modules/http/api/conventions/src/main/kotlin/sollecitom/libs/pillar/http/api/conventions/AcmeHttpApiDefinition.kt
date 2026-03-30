package sollecitom.libs.pillar.http.api.conventions

import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import sollecitom.libs.swissknife.web.api.utils.headers.of

/** The company-wide [HttpApiDefinition] with Acme-prefixed header names (e.g., `X-Acme-Invocation-Context`). */
val HttpApiDefinition.Companion.companyWide: HttpApiDefinition get() = AcmeHttpApiDefinition

internal object AcmeHttpApiDefinition : HttpApiDefinition {

    override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
}