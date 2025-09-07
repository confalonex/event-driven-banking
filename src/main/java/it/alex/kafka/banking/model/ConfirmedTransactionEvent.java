package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un evento di transazione confermata.
 * Contiene dettagli sulla transazione, gli account coinvolti,
 * l'importo, gli stati e i timestamp rilevanti.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedTransactionEvent {

    /** ID univoco della transazione */
    private String transactionId;

    /** Account di origine della transazione */
    private Account fromAccount;

    /** Account di destinazione della transazione */
    private Account toAccount;

    /** Importo della transazione */
    private BigDecimal amount;

    /** Timestamp di creazione della transazione */
    private Instant createdAt;

    /** Stato della transazione (es. "CONFIRMED", "REJECTED") */
    private String status;

    /** Stato della notifica associata alla transazione */
    private boolean valid;

    /** Motivazione se la transazione non è valida */
    private String reason;

    /** Timestamp di validazione della transazione */
    private Instant validatedAt;

    /** Timestamp di invio della notifica */
    private Instant confirmedAt;
}