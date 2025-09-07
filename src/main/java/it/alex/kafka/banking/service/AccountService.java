package it.alex.kafka.banking.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.Account;
import lombok.extern.slf4j.Slf4j;

/**
 * Servizio per la gestione degli account bancari in memoria.
 * Fornisce metodi per registrare account, recuperare account,
 * applicare trasferimenti e loggare i saldi degli account.
 */
@Service
@Slf4j
public class AccountService {

    /** Mappa concorrente per memorizzare gli account in memoria */
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Registra un nuovo account se non esiste già.
     *
     * @param a L'account da registrare
     */
    public void register(Account a) {
        if (a == null || a.getAccountId() == null) return;
        accounts.putIfAbsent(a.getAccountId(), a);
    }

    /**
     * Recupera un account in base al suo ID.
     *
     * @param accountId L'ID dell'account da recuperare
     * @return L'account corrispondente o null se non trovato
     */
    public Account get(String accountId) {
        return accounts.get(accountId);
    }

    /**
     * Recupera tutti gli account registrati.
     *
     * @return Una collezione di tutti gli account
     */
    public Collection<Account> all() {
        return accounts.values();
    }

    /**
     * Applica un trasferimento tra due account.
     *
     * @param fromId L'ID dell'account da cui prelevare i fondi
     * @param toId   L'ID dell'account a cui accreditare i fondi
     * @param amount L'importo del trasferimento
     * @return true se il trasferimento è stato applicato con successo, false altrimenti
     */
    public boolean applyTransfer(String fromId, String toId, BigDecimal amount) {
        if (fromId == null || toId == null || amount == null) return false;
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) {
            log.warn("AccountService -> account non trovato/i fromId={} toId={}", fromId, toId);
            return false;
        }
        if (from.getBalance() == null || from.getBalance().compareTo(amount) < 0) {
            log.warn("AccountService -> fondi insufficenti fromId={} amount={}", fromId, amount);
            return false;
        }
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        log.info("AccountService -> trasferimento applicato from={} to={} amount={}", fromId, toId, amount);
        return true;
    }

    /**
     * Logga i saldi di tutti gli account registrati.
     */
    public void logAllBalances() {
        accounts.values().forEach(a ->
                log.info("AccountService -> accountId={} owner={} balance={}", a.getAccountId(), a.getOwner(), a.getBalance()));
    }
}
