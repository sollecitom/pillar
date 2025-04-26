package sollecitom.libs.pillar.messaging.avro

import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

class TopicAndSerde<EVENT : Event>(val topic: Topic, val serde: AvroSerde<EVENT>, messageConverter: MessageConverter<EVENT>): MessageConverter<EVENT> by messageConverter