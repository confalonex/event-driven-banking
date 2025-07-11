package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile della ricezione e gestione degli eventi di transazione da Kafka.
 */
@Service
public class KafkaConsumerService {

    /**
     * Metodo che riceve eventi di transazione dal topic Kafka "transactions".
     *
     * @param event Evento ricevuto da Kafka
     */
    @KafkaListener(
            topics = "transactions",
            groupId = "banking-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTransaction(TransactionEvent event) {
        System.out.println("Ricevuto evento di transazione: " + event);
    }
}
