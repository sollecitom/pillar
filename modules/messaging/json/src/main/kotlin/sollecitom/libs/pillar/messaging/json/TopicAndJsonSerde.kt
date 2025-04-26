package sollecitom.libs.pillar.messaging.json

import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.messaging.domain.message.converter.MessageConverter
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

class TopicAndJsonSerde<EVENT : Event>(val topic: Topic, val serde: JsonSerde.SchemaAware<EVENT>, messageConverter: MessageConverter<EVENT>): MessageConverter<EVENT> by messageConverter