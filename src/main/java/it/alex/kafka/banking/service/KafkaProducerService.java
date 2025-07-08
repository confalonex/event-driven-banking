package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile dell'invio di eventi di transazione al topic Kafka.
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    /** Nome del topic su cui inviare i messaggi */
    private static final String TOPIC_NAME = "transactions";

    /**
     * Invia un evento di transazione al topic Kafka.
     *
     * @param event Evento da inviare
     */
    public void sendTransaction(TransactionEvent event) {
        logger.info("Invio evento Kafka: {}", event);
        kafkaTemplate.send(TOPIC_NAME, event.getTransactionId(), event);
    }
}
