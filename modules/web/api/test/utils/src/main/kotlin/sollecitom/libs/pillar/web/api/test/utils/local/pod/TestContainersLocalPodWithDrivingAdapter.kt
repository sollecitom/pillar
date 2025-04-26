package sollecitom.libs.pillar.web.api.test.utils.local.pod

import sollecitom.libs.swissknife.core.test.utils.local.pod.LocalPod
import sollecitom.libs.swissknife.kotlin.extensions.collections.circularIterator
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.web.api.test.utils.local.pod.LocalPodWithWebDrivingAdapter
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.testcontainers.containers.GenericContainer

internal class TestContainersLocalPodWithDrivingAdapter<out CONTAINER>(private val labelByInstance: Map<CONTAINER, String>) : LocalPodWithWebDrivingAdapter where CONTAINER : WithWebInterface, CONTAINER : GenericContainer<out CONTAINER> {

    constructor(instances: Set<CONTAINER>) : this(instances.withIndex().associate { it.value to it.index.toString() })

    constructor(size: Int, createInstance: () -> CONTAINER) : this(generateSequence(createInstance).take(size.also { require(it >= 1) { "A pod must contain 1 or more instances" } }).toSet())

    init {
        require(labelByInstance.values.toSet().size == labelByInstance.size) { "Cannot have duplicate labels across instances" }
    }

    private val instances get() = labelByInstance.keys
    private val iterator = instances.toList().circularIterator()
    override val size: Int get() = labelByInstance.size
    override val logs: LocalPod.Logs = Logs()

    override suspend fun start() = coroutineScope {

        logger.info { "Starting local pod with $size instances" }
        val starting = instances.map { launch { it.start() } }
        starting.joinAll()
        logger.info { "Started local pod with container IDs ${instances.map { it.containerId }}" }
    }

    override suspend fun stop() = coroutineScope {

        logger.info { "Stopping local pod with container IDs ${instances.map { it.containerId }}" }
        val stopping = instances.map { launch { it.stop() } }
        stopping.joinAll()
        logger.info { "Stopped local pod with $size instances" }
    }

    override val webInterface get() = nextInstance().webInterface

    private fun nextInstance(): CONTAINER = iterator.next()

    private inner class Logs : LocalPod.Logs {

        override fun pretty(): String = byLabel().entries.joinToString(prefix = SPACING, separator = "$DEMARCATION\n", postfix = DEMARCATION) { "Instance '${it.key}':$SPACING${it.value}" }

        override fun byLabel() = labelByInstance.values.associateWith { forInstanceWithLabel(it) }

        override fun forInstanceWithLabel(label: String): String = instances.single { label == labelByInstance[it] }.logs
    }

    companion object : Loggable() {
        private const val DEMARCATION = "\n***********\n"
        private const val SPACING = "\n\n"
    }
}

fun <CONTAINER> LocalPodWithWebDrivingAdapter.Companion.testContainers(labelByInstance: Map<CONTAINER, String>): LocalPodWithWebDrivingAdapter where CONTAINER : WithWebInterface, CONTAINER : GenericContainer<out CONTAINER> = TestContainersLocalPodWithDrivingAdapter(labelByInstance)

fun <CONTAINER> LocalPodWithWebDrivingAdapter.Companion.testContainers(instances: Set<CONTAINER>): LocalPodWithWebDrivingAdapter where CONTAINER : WithWebInterface, CONTAINER : GenericContainer<out CONTAINER> = TestContainersLocalPodWithDrivingAdapter(instances)

fun <CONTAINER> LocalPodWithWebDrivingAdapter.Companion.testContainers(size: Int, createInstance: () -> CONTAINER): LocalPodWithWebDrivingAdapter where CONTAINER : WithWebInterface, CONTAINER : GenericContainer<out CONTAINER> = TestContainersLocalPodWithDrivingAdapter(size, createInstance)