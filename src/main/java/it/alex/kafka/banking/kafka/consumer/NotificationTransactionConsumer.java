package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import it.alex.kafka.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Componente che consuma eventi di transazioni validate da un topic Kafka
 * e notifica l'utente in base alla validità della transazione.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationTransactionConsumer {

    /** Servizio per la gestione delle notifiche */
    private final NotificationService notificationService;

    /**
     * Metodo che ascolta il topic Kafka per eventi di transazioni validate.
     * Quando riceve un evento, logga le informazioni e notifica l'utente.
     *
     * @param tx Evento di transazione validata ricevuto dal topic
     */
    @KafkaListener(topics = "${app.topic.validated-transactions:validated-transactions}", groupId = "notification-group")
    public void listen(ValidatedTransactionEvent tx) {
        log.info("NotificationTransactionConsumer -> ricevuto txId={} valid={}", tx == null ? "null" : tx.getTransactionId(), tx == null ? false : tx.isValid());
        notificationService.notifyUser(tx);
    }
}
