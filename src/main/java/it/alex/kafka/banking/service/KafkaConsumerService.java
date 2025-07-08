package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile della ricezione e gestione degli eventi di transazione da Kafka.
 */
@Slf4j
@Service
public class KafkaConsumerService {

    /**
     * Metodo che riceve eventi di transazione dal topic Kafka "transactions".
     *
     * @param event Evento ricevuto da Kafka
     */
    @KafkaListener(topics = "transactions", groupId = "banking-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeTransaction(TransactionEvent event) {
        log.info("Ricevuto evento Kafka: {}", event);
        // qui potresti inserire logica di business o chiamate ad altri componenti
    }
}
