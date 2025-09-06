package it.alex.kafka.banking.service;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.kafka.producer.ValidatedInitiatedTransactionProducer;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

    private final ValidatedInitiatedTransactionProducer validatedProducer;

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