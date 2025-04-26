package sollecitom.libs.pillar.messaging.pulsar.avro

import sollecitom.libs.pillar.messaging.avro.TopicAndSerde
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.messaging.domain.message.consumer.MessageConsumer
import sollecitom.libs.swissknife.messaging.domain.message.producer.MessageProducer
import sollecitom.libs.swissknife.pulsar.avro.utils.newMessageConsumer
import sollecitom.libs.swissknife.pulsar.avro.utils.newMessageProducer
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.apache.pulsar.client.api.PulsarClient

fun <EVENT : Event> PulsarClient.newMessageConsumer(topicAndSerde: TopicAndSerde<EVENT>, instanceInfo: InstanceInfo, customize: ConsumerBuilder<EVENT>.() -> ConsumerBuilder<EVENT> = { this }): MessageConsumer<EVENT> = newMessageConsumer(topicAndSerde.serde, topicAndSerde.topic, instanceInfo, customize)

fun <EVENT : Event> PulsarClient.newMessageProducer(topicAndSerde: TopicAndSerde<EVENT>, instanceInfo: InstanceInfo, customize: ProducerBuilder<EVENT>.() -> ProducerBuilder<EVENT> = { this }): MessageProducer<EVENT> = newMessageProducer(topicAndSerde.serde, topicAndSerde.topic, instanceInfo, customize)