package it.alex.kafka.banking.service;

import java.time.Instant;
import java.util.Objects;
import org.springframework.stereotype.Service;
import it.alex.kafka.banking.kafka.producer.ValidatedInitiatedTransactionProducer;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la validazione delle transazioni.
 * <br>
 * Verifica che le transazioni rispettino determinate regole di validità
 * e invia gli eventi di transazione validata al topic Kafka appropriato.
 */
@Service
@Slf4j
public class ValidationService {

    /** Produttore Kafka per inviare transazioni validate */
    private final ValidatedInitiatedTransactionProducer validatedProducer;

    public ValidationService(ValidatedInitiatedTransactionProducer validatedProducer) {
        this.validatedProducer = validatedProducer;
    }

    /**
     * Valida una transazione e invia l'evento di transazione validata.<br>
     * Le regole di validità includono:<br>
     * - L'importo deve essere positivo.<br>
     * - Gli account di origine e destinazione non devono essere nulli.<br>
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

        ValidatedTransactionEvent v = new ValidatedTransactionEvent(
                tx.getTransactionId(),
                tx.getFromAccount(),
                tx.getToAccount(),
                tx.getAmount(),
                tx.getCreatedAt(),
                tx.getStatus(),
                valid,
                reason,
                Instant.now()
        );

        log.info("ValidationService -> txId={} valid={} reason={}", tx.getTransactionId(), valid, reason);
        validatedProducer.sendValidated(v);
    }
}