package it.alex.kafka.banking.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.kafka.producer.ConfirmedTransactionProducer;
import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import it.alex.kafka.banking.model.NotificationTransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la gestione delle notifiche agli utenti.<br>
 * Riceve eventi di transazioni validate, notifica gli utenti e produce eventi di transazioni confermate.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    /** Servizio per la registrazione delle notifiche */
    private final NotificationRegistryService registry;

    /** Produttore Kafka per le transazioni confermate */
    private final ConfirmedTransactionProducer confirmedProducer;

    /**
     * Notifica l'utente di una transazione validata e produce un evento di transazione confermata.
     *
     * @param v Evento di transazione validata
     */
    public void notifyUser(ValidatedTransactionEvent v) {
        if (v == null || !v.isValid()) {
            log.info("NotificationService -> notifica saltata per txId={} valid={}",
                    v == null ? "null" : v.getTransactionId(), v == null ? false : v.isValid());
            return;
        }

        NotificationTransactionEvent n = NotificationTransactionEvent.builder()
                .transactionId(v.getTransactionId())
                .fromAccount(v.getFromAccount())
                .toAccount(v.getToAccount())
                .amount(v.getAmount())
                .createdAt(v.getCreatedAt())
                .status("SENT")
                .valid(true)
                .reason(null)
                .validatedAt(Instant.now())
                .notificationStatus("SENT")
                .sentAt(Instant.now())
                .readAt(null)
                .build();

        log.info("NotificationService -> notifica inviata per txId={} all'utente={}",
                n.getTransactionId(), n.getFromAccount().getOwner());
        registry.register(n);

        registry.markRead(n.getTransactionId());

        ConfirmedTransactionEvent c = ConfirmedTransactionEvent.builder()
                .transactionId(n.getTransactionId())
                .fromAccount(n.getFromAccount())
                .toAccount(n.getToAccount())
                .amount(n.getAmount())
                .createdAt(n.getCreatedAt())
                .status("CONFIRMED")
                .valid(n.isValid())
                .reason(n.getReason())
                .validatedAt(n.getValidatedAt())
                .confirmedAt(Instant.now())
                .build();

        confirmedProducer.sendConfirmed(c);
    }
}
