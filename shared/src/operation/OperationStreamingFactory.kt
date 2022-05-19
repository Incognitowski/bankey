package operation

import configuration.KafkaConfigurationDTO
import framework.extensionFunctions.parseFromJson
import framework.injectFromKoin
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.Serdes.LongSerde
import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import java.util.*

object OperationStreamingFactory {

    fun getProducer(): Producer<Long, String> {
        val lKafkaConfigurationDTO: KafkaConfigurationDTO = injectFromKoin()
        val lProducerProperties = Properties()
        lProducerProperties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
        lProducerProperties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        lProducerProperties["bootstrap.servers"] = lKafkaConfigurationDTO.bootstrapServers
        lProducerProperties["sasl.jaas.config"] = lKafkaConfigurationDTO.jaasConfig
        lProducerProperties["security.protocol"] = "SASL_SSL"
        lProducerProperties["sasl.mechanism"] = "PLAIN"
        lProducerProperties["client.dns.lookup"] = "use_all_dns_ips"
        lProducerProperties["session.timeout.ms"] = 45000
        lProducerProperties["acks"] = "all"
        return KafkaProducer(lProducerProperties)
    }

    fun getConsumer(): Consumer<Long, String> {
        val lKafkaConfigurationDTO: KafkaConfigurationDTO = injectFromKoin()
        val lProducerProperties = Properties()
        lProducerProperties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
        lProducerProperties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        lProducerProperties["bootstrap.servers"] = lKafkaConfigurationDTO.bootstrapServers
        lProducerProperties["sasl.jaas.config"] = lKafkaConfigurationDTO.jaasConfig
        lProducerProperties["security.protocol"] = "SASL_SSL"
        lProducerProperties["sasl.mechanism"] = "PLAIN"
        lProducerProperties["client.dns.lookup"] = "use_all_dns_ips"
        lProducerProperties["session.timeout.ms"] = 45000
        lProducerProperties["acks"] = "all"
        return KafkaConsumer(lProducerProperties)
    }

    fun getStream(aOperationProtocol: String): KStream<Long, String> {
        return StreamsBuilder()
            .stream("operations", Consumed.with(LongSerde(), StringSerde()))
            .filter { _, value ->
                val lOperationEntity = value.parseFromJson<OperationEntity>()
                lOperationEntity.protocol == aOperationProtocol
            }
    }

}