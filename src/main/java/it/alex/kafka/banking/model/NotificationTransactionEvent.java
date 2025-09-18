package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Rappresenta un evento di notifica di una transazione.
 * Contiene informazioni sulla transazione, lo stato della notifica e i timestamp rilevanti.
 */
public class NotificationTransactionEvent {

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

    /** Stato della transazione (es. "SENT", "READ") */
    private String status;

    /** Validità della transazione */
    private boolean valid;

    /** Motivazione se la transazione non è valida */
    private String reason;

    /** Timestamp di validazione della transazione */
    private Instant validatedAt;

    /** Stato della notifica (es. "SENT", "READ") */
    private String notificationStatus;

    /** Timestamp di invio della notifica */
    private Instant sentAt;

    /** Timestamp di lettura della notifica */
    private Instant readAt;

    public NotificationTransactionEvent() {
    }

    public NotificationTransactionEvent(String transactionId, Account fromAccount, Account toAccount, BigDecimal amount, Instant createdAt, String status, boolean valid, String reason, Instant validatedAt, String notificationStatus, Instant sentAt, Instant readAt) {
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.createdAt = createdAt;
        this.status = status;
        this.valid = valid;
        this.reason = reason;
        this.validatedAt = validatedAt;
        this.notificationStatus = notificationStatus;
        this.sentAt = sentAt;
        this.readAt = readAt;
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

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }
}