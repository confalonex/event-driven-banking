package it.alex.kafka.banking.model;

import java.math.BigDecimal;

/**
 * Rappresenta un account bancario con ID, proprietario e saldo.
 */
public class Account {

    /** ID univoco dell'account */
    private String accountId;

    /** Nome del proprietario dell'account */
    private String owner;

    /** Saldo corrente dell'account */
    private BigDecimal balance;

    public Account() {
    }

    public Account(String accountId, String owner, BigDecimal balance) {
        this.accountId = accountId;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}