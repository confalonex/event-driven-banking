package it.alex.kafka.banking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * Rappresenta un evento di transazione bancaria da inviare tramite Kafka.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionEvent {

    /** Identificativo univoco della transazione */
    private String transactionId;

    /** Identificativo del conto associato */
    private String accountId;

    /** Importo della transazione */
    private double amount;

    /** Tipo dell’operazione */
    private String type;

    /** Timestamp di generazione dell’evento */
    private LocalDateTime timestamp;
}
