package sollecitom.libs.pillar.acme.business.domain

import sollecitom.libs.pillar.acme.conventions.CompanyConventions
import sollecitom.libs.pillar.http.api.conventions.companyWide
import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.pillar.acme.business.domain.Example.defaultLocale
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.localeOrNull
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.web.api.utils.api.HttpApiDefinition
import java.util.*

object Acme : CompanyConventions, MessagePropertyNames by AcmeMessagePropertyNames, HttpApiDefinition by HttpApiDefinition.companyWide {

    val tenant = Tenant(id = StringId("acme.com"))
    val roles get() = AcmeRoles

    override val defaultLocale: Locale = Locale.UK
}

context(context: InvocationContext<*>)
val locale: Locale get() = context.localeOrNull ?: defaultLocale