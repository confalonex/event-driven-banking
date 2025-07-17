package it.alex.kafka.banking.service;

import it.alex.kafka.banking.config.Topics;
import it.alex.kafka.banking.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile dell'invio di eventi di transazione al topic Kafka.
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    /**
     * Template Kafka per l'invio di messaggi.
     * Utilizza una chiave di tipo String e un valore di tipo TransactionEvent.
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Invia un evento di transazione serializzato come JSON al topic Kafka configurato.
     * La chiave del messaggio è l'ID della transazione.
     *
     * @param event evento di transazione da inviare
     */
    public void sendTransaction(TransactionEvent event) {
        kafkaTemplate.send(Topics.TRANSACTIONS, event.getTransactionId(), event);
    }
}
