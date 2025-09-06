package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private Instant createdAt;
    private String status; // e.g. INITIATED, VALIDATED, NOTIFIED, CONFIRMED, REJECTED
}