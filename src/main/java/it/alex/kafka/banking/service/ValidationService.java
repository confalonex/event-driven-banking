package it.alex.kafka.banking.service;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.kafka.producer.ValidatedInitiatedTransactionProducer;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la validazione delle transazioni.
 * Verifica che le transazioni rispettino determinate regole di validità
 * e invia gli eventi di transazione validata al topic Kafka appropriato.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    /** Produttore Kafka per inviare transazioni validate */
    private final ValidatedInitiatedTransactionProducer validatedProducer;

    /**
     * Valida una transazione e invia l'evento di transazione validata.
     * Le regole di validità includono:
     * - L'importo deve essere positivo.
     * - Gli account di origine e destinazione non devono essere nulli.
     * - Gli account di origine e destinazione non devono essere gli stessi.
     *
     * @param tx L'evento di transazione da validare
     */
    public void validate(TransactionEvent tx) {
        if (tx == null) return;

        boolean valid = true;
        String reason = null;

        if (tx.getAmount() == null || tx.getAmount().signum() <= 0) {
            valid = false;
            reason = "amount-invalid";
        } else if (tx.getFromAccount() == null || tx.getToAccount() == null) {
            valid = false;
            reason = "missing-account";
        } else if (Objects.equals(tx.getFromAccount().getAccountId(), tx.getToAccount().getAccountId())) {
            valid = false;
            reason = "same-account";
        }

        ValidatedTransactionEvent v = ValidatedTransactionEvent.builder()
                .transactionId(tx.getTransactionId())
                .fromAccount(tx.getFromAccount())
                .toAccount(tx.getToAccount())
                .amount(tx.getAmount())
                .createdAt(tx.getCreatedAt())
                .status(tx.getStatus())
                .valid(valid)
                .reason(reason)
                .validatedAt(Instant.now())
                .build();

        log.info("ValidationService -> txId={} valid={} reason={}", tx.getTransactionId(), valid, reason);
        validatedProducer.sendValidated(v);
    }
}