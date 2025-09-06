package it.alex.kafka.banking.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.Account;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public void register(Account a) {
        if (a == null || a.getAccountId() == null) return;
        accounts.putIfAbsent(a.getAccountId(), a);
    }

    public Account get(String accountId) {
        return accounts.get(accountId);
    }

    public Collection<Account> all() {
        return accounts.values();
    }

    public boolean applyTransfer(String fromId, String toId, BigDecimal amount) {
        if (fromId == null || toId == null || amount == null) return false;
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) {
            log.warn("AccountService -> account(s) not found fromId={} toId={}", fromId, toId);
            return false;
        }
        if (from.getBalance() == null || from.getBalance().compareTo(amount) < 0) {
            log.warn("AccountService -> insufficient funds fromId={} amount={}", fromId, amount);
            return false;
        }
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        log.info("AccountService -> transfer applied from={} to={} amount={}", fromId, toId, amount);
        return true;
    }

    public void logAllBalances() {
        accounts.values().forEach(a ->
                log.info("AccountService -> accountId={} owner={} balance={}", a.getAccountId(), a.getOwner(), a.getBalance()));
    }
}
