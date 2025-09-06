package it.alex.kafka.banking.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.NotificationTransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRegistryService registry;

    /**
     * Simula l'invio della notifica e la registra per il cron che la confermerà.
     * Non produce la conferma qui: quella la fa il scheduler/ConfirmationService.
     * Qui si limita a creare l'evento di notifica e registrarlo.
     */
    public void notifyUser(ValidatedTransactionEvent v) {
        if (v == null || !v.isValid()) {
            log.info("NotificationService -> skip notify for txId={} valid={}",
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

        log.info("ValidationService -> sent notification for txId={} to user={}",
                n.getTransactionId(), n.getFromAccount().getOwner());
        registry.register(n);
    }
}