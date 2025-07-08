package it.alex.kafka.banking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.alex.kafka.banking.model.TransactionEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Configurazione generale per Kafka: producer, consumer e listener container.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // 🔹 Producer config
    @Bean
    public ProducerFactory<String, TransactionEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, TransactionEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // 🔹 Consumer config
    @Bean
    public ConsumerFactory<String, TransactionEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "banking-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
        config.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    // 🔹 Listener factory per @KafkaListener
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
