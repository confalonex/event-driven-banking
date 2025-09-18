package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Componente che consuma gli eventi di transazione dal topic Kafka e li valida.
 */
@Component
@Slf4j
public class ValidationTransactionConsumer {

    /** Servizio per la validazione delle transazioni */
    private final ValidationService validationService;

    public ValidationTransactionConsumer(ValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Metodo che ascolta il topic Kafka per gli eventi di transazione e li valida.
     *
     * @param tx Evento di transazione ricevuto dal topic
     */
    @KafkaListener(topics = "${app.topic.transactions:transactions}", groupId = "validation-group")
    public void listen(TransactionEvent tx) {
        log.info("ValidationTransactionConsumer -> ricevuto txId={}", tx == null ? "null" : tx.getTransactionId());
        validationService.validate(tx);
    }
}
