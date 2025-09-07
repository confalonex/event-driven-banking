package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un evento di transazione.
 * Contiene informazioni sulla transazione, gli account coinvolti, l'importo,
 * il timestamp di creazione e lo stato della transazione.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}