package sollecitom.libs.pillar.service.container_based.test

import sollecitom.libs.pillar.acme.conventions.ContainerBasedTestConventions
import sollecitom.libs.pillar.acme.conventions.LoggingConventions
import sollecitom.libs.pillar.service.logging.serviceStartedLogMessage
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.Wait

context(_: ContainerBasedTestConventions, _: LoggingConventions)
fun <CONTAINER : GenericContainer<CONTAINER>> CONTAINER.waitingForServiceStarted(): CONTAINER = waitingFor(waitForServiceStartedLogMessage())

context(_: ContainerBasedTestConventions, _: LoggingConventions)
private fun waitForServiceStartedLogMessage(): LogMessageWaitStrategy = Wait.forLogMessage(".*$serviceStartedLogMessage.*", 1)