package it.alex.kafka.banking.service;

import it.alex.kafka.banking.config.Topics;
import it.alex.kafka.banking.model.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile della ricezione e gestione degli eventi di transazione da Kafka.
 */
@Service
@Slf4j
public class KafkaConsumerService {

    /**
     * Metodo che riceve eventi di transazione dal topic Kafka "transactions".
     *
     * @param event Evento ricevuto da Kafka
     */
    @KafkaListener(topics = Topics.TRANSACTIONS)
    public void consumeTransaction(TransactionEvent event) {
        log.info("Ricevuto evento di transazione: {}", event);
    }
}
