package sollecitom.libs.pillar.messaging.conventions

import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames

/** Acme-specific message property names, defining how event metadata is encoded in message headers. */
object AcmeMessagePropertyNames : MessagePropertyNames {

    override val forEvents: MessagePropertyNames.ForEvents get() = EventMessagePropertyNames

    private object EventMessagePropertyNames : MessagePropertyNames.ForEvents {

        override val type = "event-type"
    }
}