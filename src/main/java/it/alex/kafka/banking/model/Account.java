package it.alex.kafka.banking.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un account bancario con ID, proprietario e saldo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    /** ID univoco dell'account */
    private String accountId;

    /** Nome del proprietario dell'account */
    private String owner;

    /** Saldo corrente dell'account */
    private BigDecimal balance;
}