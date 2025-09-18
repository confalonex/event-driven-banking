package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Produttore Kafka per inviare eventi di transazioni confermate.
 */
@Component
@Slf4j
public class ConfirmedTransactionProducer {

    /** Template Kafka per inviare messaggi */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /** Nome del topic Kafka per le transazioni confermate */
    private final String confirmedTopic;

    public ConfirmedTransactionProducer(KafkaTemplate<String, Object> kafkaTemplate, @Value("${app.topic.confirmed-transactions:confirmed-transactions}") String confirmedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.confirmedTopic = confirmedTopic;
    }

    /** Invia un evento di transazione confermata al topic Kafka.
     *
     * @param tx Evento di transazione confermata da inviare
     */
    public void sendConfirmed(ConfirmedTransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("ConfirmedTransactionProducer -> inviato txId={}", tx.getTransactionId());
        kafkaTemplate.send(confirmedTopic, tx.getTransactionId(), tx);
    }
}
