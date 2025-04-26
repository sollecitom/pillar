package sollecitom.libs.pillar.http.api.conventions

import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import sollecitom.libs.swissknife.web.api.utils.headers.HttpHeaderNames
import sollecitom.libs.swissknife.web.api.utils.headers.of

val HttpApiDefinition.Companion.companyWide: HttpApiDefinition get() = AcmeHttpApiDefinition

internal object AcmeHttpApiDefinition : HttpApiDefinition {

    override val headerNames: HttpHeaderNames = HttpHeaderNames.of(companyName = "acme")
}