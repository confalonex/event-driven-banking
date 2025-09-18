package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Componente per l'invio di eventi di transazioni iniziate a un topic Kafka.
 */
@Component
@Slf4j
public class InitiatedTransactionProducer {

    /** Template Kafka per l'invio dei messaggi */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /** Nome del topic Kafka per le transazioni */
    private final String transactionsTopic;

    public InitiatedTransactionProducer(KafkaTemplate<String, Object> kafkaTemplate, @Value("${app.topic.transactions:transactions}") String transactionsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionsTopic = transactionsTopic;
    }

    /** Invia un evento di transazione iniziata al topic Kafka.
     *
     * @param tx Evento di transazione da inviare
     */
    public void sendInitiated(TransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("InitiatedTransactionProducer -> inviato txId={}", tx.getTransactionId());
        kafkaTemplate.send(transactionsTopic, tx.getTransactionId(), tx);
    }
}
