package sollecitom.libs.pillar.correlation.logging.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.wasForkedFrom
import sollecitom.libs.swissknife.json.utils.getJSONObjectOrNull
import sollecitom.libs.swissknife.kotlin.extensions.text.removeFromLast
import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde

fun Assert<List<String>>.haveContextForkedFrom(invocationContext: InvocationContext<*>, allowEmpty: Boolean = false) = given { logs ->

    if (!allowEmpty) {
        assertThat(logs).isNotEmpty()
    }
    logs.forEach { logLine ->
        val context = extractInvocationContext(logLine)
        assertThat(context).isNotNull().wasForkedFrom(invocationContext)
    }
}

fun Assert<List<String>>.haveContext(invocationContext: InvocationContext<*>, allowEmpty: Boolean = false) = given { logs ->

    if (!allowEmpty) {
        assertThat(logs).isNotEmpty()
    }
    logs.forEach { logLine ->
        val context = extractInvocationContext(logLine)
        assertThat(context).isNotNull().isEqualTo(invocationContext)
    }
}

private fun extractInvocationContext(logLine: String): InvocationContext<*>? {

    return logLine.runCatching { JSONObject(this) }.map { json ->
        json.getJSONObjectOrNull("context")?.getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize)
    }.getOrElse {
        val rawContext = logLine.removeFromLast(" - context: ").takeUnless { it == logLine }?.let { logLine.removePrefix(it).removePrefix(" - context: ") }
        rawContext?.let { JSONObject(it).getJSONObjectOrNull("invocation")?.let(InvocationContext.jsonSerde::deserialize) }
    }
}