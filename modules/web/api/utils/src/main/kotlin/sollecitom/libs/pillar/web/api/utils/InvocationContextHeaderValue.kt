package sollecitom.libs.pillar.web.api.utils

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import java.util.Base64

/** Encodes this context as an HTTP header value: the base64url encoding (without padding) of its UTF-8 JSON. */
fun InvocationContext<*>.toHeaderValue(): String = InvocationContext.jsonSerde.serialize(this).toString().toByteArray().let(Base64.getUrlEncoder().withoutPadding()::encodeToString)

/** Decodes a context from an HTTP header value produced by [toHeaderValue], rejecting JSON that doesn't match the context's schema. */
fun InvocationContext.Companion.fromHeaderValue(value: String): InvocationContext<Access> {

    val json = Base64.getUrlDecoder().decode(value).decodeToString().let(::JSONObject)
    InvocationContext.jsonSerde.schema.validate(json)?.let { failure -> throw IllegalArgumentException("The invocation context doesn't match its schema: ${failure.message}") }
    return InvocationContext.jsonSerde.deserialize(json)
}
