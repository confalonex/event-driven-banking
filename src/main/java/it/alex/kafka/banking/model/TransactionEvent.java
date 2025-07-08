package it.alex.kafka.banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Rappresenta un evento di transazione bancaria da inviare tramite Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {

    /** Identificativo univoco della transazione */
    private String transactionId;

    /** Identificativo del conto associato */
    private String accountId;

    /** Importo della transazione */
    private double amount;

    /** Tipo dell’operazione (es. "deposit", "withdrawal") */
    private String type;

    /** Timestamp di generazione dell’evento */
    private LocalDateTime timestamp;
}
