package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un evento di notifica di una transazione.
 * Contiene informazioni sulla transazione, lo stato della notifica e i timestamp rilevanti.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}