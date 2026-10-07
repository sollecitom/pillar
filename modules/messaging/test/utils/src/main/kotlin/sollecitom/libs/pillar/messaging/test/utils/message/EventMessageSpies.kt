package sollecitom.libs.pillar.messaging.test.utils.message

import sollecitom.libs.pillar.messaging.conventions.AcmeMessagePropertyNames
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.messaging.test.utils.message.inMemorySpy
import sollecitom.libs.swissknife.messaging.test.utils.message.ulid
import sollecitom.libs.swissknife.messaging.test.utils.topic.create
import kotlin.time.Instant

context(_: CoreDataGenerator)
fun <EVENT : Event> EVENT.asReceivedEventSpy(propertyNames: MessagePropertyNames = AcmeMessagePropertyNames) = ReceivedMessage.inMemorySpy(this, properties = mapOf(propertyNames.forEvents.type to type.stringValue))

class UndecodableMessageSpy<EVENT : Event>(override val id: Message.Id, override val properties: Map<String, String>, override val producerName: Name, override val publishedAt: Instant) : ReceivedMessage<EVENT> {

    var wasAcknowledged = false
        private set
    override val key: String? = null
    override val value: EVENT get() = error("This payload cannot be decoded")
    override val rawData = byteArrayOf(0x00)
    override val context = Message.Context()

    override suspend fun acknowledge() {
        wasAcknowledged = true
    }

    companion object
}

context(_: CoreDataGenerator)
fun <EVENT : Event> UndecodableMessageSpy.Companion.withType(type: Happening.Type, propertyNames: MessagePropertyNames = AcmeMessagePropertyNames) = undecodableMessageSpy<EVENT>(properties = mapOf(propertyNames.forEvents.type to type.stringValue))

context(_: CoreDataGenerator)
fun <EVENT : Event> UndecodableMessageSpy.Companion.withoutType() = undecodableMessageSpy<EVENT>(properties = emptyMap())

context(generator: CoreDataGenerator)
private fun <EVENT : Event> undecodableMessageSpy(properties: Map<String, String>) = UndecodableMessageSpy<EVENT>(id = Message.Id.ulid(topic = Topic.create()), properties = properties, producerName = Name.random(), publishedAt = generator.clock.now())
