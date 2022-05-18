package operation

import configuration.KafkaConfigurationDTO
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class OperationStreamingAPI(mKafkaConfigurationDTO: KafkaConfigurationDTO) {

    private val producer: Producer<Long, String>

    init {
        val props = Properties()
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        props["bootstrap.servers"] = mKafkaConfigurationDTO.bootstrapServers
        props["security.protocol"] = "SASL_SSL"
        props["sasl.jaas.config"] = mKafkaConfigurationDTO.jaasConfig
        props["sasl.mechanism"] = "PLAIN"
        props["client.dns.lookup"] = "use_all_dns_ips"
        props["session.timeout.ms"] = 45000
        props["acks"] = "all"
        producer = KafkaProducer(props)
    }

}