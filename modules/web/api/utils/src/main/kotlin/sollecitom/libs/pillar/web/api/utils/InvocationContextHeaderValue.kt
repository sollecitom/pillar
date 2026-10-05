package sollecitom.libs.pillar.web.api.utils

import org.json.JSONObject
import sollecitom.libs.pillar.json.serialization.correlation.core.context.jsonSerde
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import java.util.Base64

/** Encodes this context as an HTTP header value: the base64url encoding (without padding) of its UTF-8 JSON. */
fun InvocationContext<*>.toHeaderValue(): String = Base64.getUrlEncoder().withoutPadding().encodeToString(InvocationContext.jsonSerde.serialize(this).toString().toByteArray())

/** Decodes a context from an HTTP header value produced by [toHeaderValue]. */
fun InvocationContext.Companion.fromHeaderValue(value: String): InvocationContext<Access> = InvocationContext.jsonSerde.deserialize(JSONObject(Base64.getUrlDecoder().decode(value).decodeToString()))
