package sollecitom.libs.pillar.acme.conventions

/** Aggregates all company-wide convention interfaces. Implement this to adopt the full set of Acme conventions. */
interface CompanyConventions : MonitoringConventions, LoggingConventions, ContainerBasedTestConventions, MessagingConventions, LocaleConventions, HttpApiConventions