package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rappresenta un evento di transazione validata.
 * Contiene informazioni sulla transazione, gli account coinvolti, l'importo,
 * il timestamp di creazione, lo stato della transazione, la validità e i motivi di eventuale invalidità.
 */
public class ValidatedTransactionEvent {

    /** ID univoco della transazione */
    private String transactionId;

    /** Account di origine della transazione */
    private Account fromAccount;

    /** Account di destinazione della transazione */
    private Account toAccount;

    /** Importo della transazione */
    private BigDecimal amount;

    /** Timestamp di creazione dell'evento */
    private Instant createdAt;

    /** Stato della transazione (es. "INITIATED", "VALIDATED", "NOTIFIED", "CONFIRMED", "REJECTED") */
    private String status;

    /** Validità della transazione */
    private boolean valid;

    /** Motivazione se la transazione non è valida */
    private String reason;

    /** Timestamp di validazione della transazione */
    private Instant validatedAt;

    public ValidatedTransactionEvent() {
    }

    public ValidatedTransactionEvent(String transactionId, Account fromAccount, Account toAccount, BigDecimal amount, Instant createdAt, String status, boolean valid, String reason, Instant validatedAt) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdAt = createdAt;
        this.status = status;
        this.valid = valid;
        this.reason = reason;
        this.validatedAt = validatedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(Instant validatedAt) {
        this.validatedAt = validatedAt;
    }
}