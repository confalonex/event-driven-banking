package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rappresenta un evento di transazione.<br>
 * Contiene informazioni sulla transazione, gli account coinvolti, l'importo,
 * il timestamp di creazione e lo stato della transazione.
 */
public class TransactionEvent {

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

    public TransactionEvent() {
    }

    public TransactionEvent(String transactionId, Account fromAccount, Account toAccount, BigDecimal amount, Instant createdAt, String status) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdAt = createdAt;
        this.status = status;
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
}