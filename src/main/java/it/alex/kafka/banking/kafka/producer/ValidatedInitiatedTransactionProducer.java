package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Componente per l'invio di eventi di transazioni validate al topic Kafka.
 */
@Component
@Slf4j
public class ValidatedInitiatedTransactionProducer {

    /** Template Kafka per l'invio dei messaggi */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /** Nome del topic Kafka per le transazioni validate */
    private final String validatedTopic;

    public ValidatedInitiatedTransactionProducer(KafkaTemplate<String, Object> kafkaTemplate, @Value("${app.topic.validated-transactions:validated-transactions}") String validatedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.validatedTopic = validatedTopic;
    }

    /** Invia un evento di transazione validata al topic Kafka.
     *
     * @param tx Evento di transazione validata da inviare
     */
    public void sendValidated(ValidatedTransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("ValidatedInitiatedTransactionProducer -> inviato txId={} valid={}", tx.getTransactionId(), tx.isValid());
        kafkaTemplate.send(validatedTopic, tx.getTransactionId(), tx);
    }
}
