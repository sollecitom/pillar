package sollecitom.libs.pillar.service.logging

import sollecitom.libs.pillar.acme.conventions.LoggingConventions
import sollecitom.libs.swissknife.logger.core.loggable.Loggable

private const val SERVICE_STARTED_LOG_MESSAGE = "--Service started--"
private const val SERVICE_STOPPED_LOG_MESSAGE = "--Service stopped--"

context(LoggingConventions)
val serviceStartedLogMessage: String
    get() = SERVICE_STARTED_LOG_MESSAGE

context(LoggingConventions)
val serviceStoppedLogMessage: String
    get() = SERVICE_STOPPED_LOG_MESSAGE

context(LoggingConventions)
fun Loggable.logServiceStarted() = logger.info { serviceStartedLogMessage }

context(LoggingConventions)
fun Loggable.logServiceStopped() = logger.info { serviceStoppedLogMessage }