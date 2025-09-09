package it.alex.kafka.banking.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.NotificationTransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la gestione delle notifiche agli utenti.<br>
 * Simula l'invio di notifiche e registra gli eventi di notifica.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    /** Servizio per la registrazione delle notifiche */
    private final NotificationRegistryService registry;

    /**
     * Simula l'invio della notifica e la registra per il cron che la confermerà.<br>
     * Crea l'evento di notifica e lo registra.
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
    }
}