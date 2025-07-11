package it.alex.kafka.banking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.alex.kafka.banking.model.SecurityAlertEvent;
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
 * Configura i componenti Kafka per la produzione e il consumo di eventi.
 * Include configurazioni separate per TransactionEvent e SecurityAlertEvent.
 */
@EnableKafka
@Configuration
public class KafkaConfig {

    /**
     * Indirizzo del broker Kafka.
     * In un ambiente di produzione, questo dovrebbe essere configurato tramite variabili d'ambiente o file di configurazione.
     */
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    // ─────────────────────────────────────────────
    // PRODUCER per TransactionEvent
    // ─────────────────────────────────────────────

    /**
     * Configura un Producer Kafka per serializzare e inviare eventi di tipo TransactionEvent.
     *
     * @return una ProducerFactory configurata per TransactionEvent.
     */
    @Bean
    public ProducerFactory<String, TransactionEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Crea un KafkaTemplate per inviare eventi TransactionEvent al broker Kafka.
     *
     * @return il KafkaTemplate per TransactionEvent.
     */
    @Bean
    public KafkaTemplate<String, TransactionEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ─────────────────────────────────────────────
    // CONSUMER per TransactionEvent
    // ─────────────────────────────────────────────

    /**
     * Configura un Consumer Kafka per deserializzare eventi di tipo TransactionEvent.
     *
     * @return una ConsumerFactory con le impostazioni per TransactionEvent.
     */
    @Bean
    public ConsumerFactory<String, TransactionEvent> transactionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "banking-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
        config.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * Crea una factory Kafka per gestire l’ascolto di eventi TransactionEvent.
     *
     * @return una ConcurrentKafkaListenerContainerFactory per TransactionEvent.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
    }

    // ─────────────────────────────────────────────
    // PRODUCER per SecurityAlertEvent
    // ─────────────────────────────────────────────

    /**
     * Configura un Producer Kafka per serializzare e inviare eventi di tipo SecurityAlertEvent.
     *
     * @return una ProducerFactory configurata per SecurityAlertEvent.
     */
    @Bean
    public ProducerFactory<String, SecurityAlertEvent> securityAlertProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Crea un KafkaTemplate per inviare eventi SecurityAlertEvent al broker Kafka.
     *
     * @return il KafkaTemplate per SecurityAlertEvent.
     */
    @Bean
    public KafkaTemplate<String, SecurityAlertEvent> securityAlertKafkaTemplate() {
        return new KafkaTemplate<>(securityAlertProducerFactory());
    }

    // ─────────────────────────────────────────────
    // CONSUMER per SecurityAlertEvent
    // ─────────────────────────────────────────────

    /**
     * Configura un Consumer Kafka per deserializzare eventi di tipo SecurityAlertEvent.
     *
     * @return una ConsumerFactory con le impostazioni per SecurityAlertEvent.
     */
    @Bean
    public ConsumerFactory<String, SecurityAlertEvent> securityAlertConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "banking-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
        config.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * Crea una factory Kafka per gestire l’ascolto di eventi SecurityAlertEvent.
     *
     * @return una ConcurrentKafkaListenerContainerFactory per SecurityAlertEvent.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SecurityAlertEvent> securityAlertListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SecurityAlertEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(securityAlertConsumerFactory());
        return factory;
    }
}
