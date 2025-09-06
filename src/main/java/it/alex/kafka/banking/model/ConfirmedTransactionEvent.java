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
public class ConfirmedTransactionEvent {
    private String transactionId;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private Instant createdAt;
    private String status;
    private boolean valid;
    private String reason; // breve motivazione se invalid
    private Instant validatedAt;
    private Instant confirmedAt;
}