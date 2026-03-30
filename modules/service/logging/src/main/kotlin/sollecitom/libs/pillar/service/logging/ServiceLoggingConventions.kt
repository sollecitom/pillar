package sollecitom.libs.pillar.service.logging

import sollecitom.libs.pillar.acme.conventions.LoggingConventions
import sollecitom.libs.swissknife.logger.core.loggable.Loggable

private const val SERVICE_STARTED_LOG_MESSAGE = "--Service started--"
private const val SERVICE_STOPPED_LOG_MESSAGE = "--Service stopped--"

/** Standard log message emitted when a service starts. */
context(_: LoggingConventions)
val serviceStartedLogMessage: String
    get() = SERVICE_STARTED_LOG_MESSAGE

/** Standard log message emitted when a service stops. */
context(_: LoggingConventions)
val serviceStoppedLogMessage: String
    get() = SERVICE_STOPPED_LOG_MESSAGE

/** Logs the standard service-started message at INFO level. */
context(_: LoggingConventions)
fun Loggable.logServiceStarted() = logger.info { serviceStartedLogMessage }

/** Logs the standard service-stopped message at INFO level. */
context(_: LoggingConventions)
fun Loggable.logServiceStopped() = logger.info { serviceStoppedLogMessage }