package sollecitom.libs.pillar.messaging.domain.event.processing

import sollecitom.libs.swissknife.messaging.domain.message.Message

class UndecodableMessageException(val messageId: Message.Id, val key: String?, val eventType: String?, cause: Throwable) : RuntimeException("Message with ID ${messageId.stringRepresentation}, key $key, and event type $eventType cannot be decoded", cause)
