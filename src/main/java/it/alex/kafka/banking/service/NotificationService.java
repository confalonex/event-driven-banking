package it.alex.kafka.banking.service;

import java.time.Instant;
import org.springframework.stereotype.Service;
import it.alex.kafka.banking.kafka.producer.ConfirmedTransactionProducer;
import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import it.alex.kafka.banking.model.NotificationTransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la gestione delle notifiche agli utenti.<br>
 * Riceve eventi di transazioni validate, notifica gli utenti e produce eventi di transazioni confermate.
 */
@Service
@Slf4j
public class NotificationService {

    /** Servizio per la registrazione delle notifiche */
    private final NotificationRegistryService registry;

    /** Produttore Kafka per le transazioni confermate */
    private final ConfirmedTransactionProducer confirmedProducer;

    public NotificationService(NotificationRegistryService registry, ConfirmedTransactionProducer confirmedProducer) {
        this.registry = registry;
        this.confirmedProducer = confirmedProducer;
    }

    /**
     * Notifica l'utente di una transazione validata e produce un evento di transazione confermata.
     *
     * @param v Evento di transazione validata
     */
    public void notifyUser(ValidatedTransactionEvent v) {
        if (v == null || !v.isValid()) {
            log.info("NotificationService -> notifica saltata per txId={} valid={}",
                    v == null ? "null" : v.getTransactionId(), v != null && v.isValid());
            return;
        }

        NotificationTransactionEvent n = new NotificationTransactionEvent(
                v.getTransactionId(),
                v.getFromAccount(),
                v.getToAccount(),
                v.getAmount(),
                v.getCreatedAt(),
                "SENT",
                true,
                null,
                Instant.now(),
                "SENT",
                Instant.now(),
                null
        );

        log.info("NotificationService -> notifica inviata per txId={} all'utente={}",
                n.getTransactionId(), n.getFromAccount().getOwner());
        registry.register(n);

        registry.markRead(n.getTransactionId());

        // commenta tutta questa parte se non vuoi confermare automaticamente
        ConfirmedTransactionEvent c = new ConfirmedTransactionEvent(
                n.getTransactionId(),
                n.getFromAccount(),
                n.getToAccount(),
                n.getAmount(),
                n.getCreatedAt(),
                "CONFIRMED",
                n.isValid(),
                n.getReason(),
                n.getValidatedAt(),
                Instant.now()
        );

        confirmedProducer.sendConfirmed(c);
    }
}
