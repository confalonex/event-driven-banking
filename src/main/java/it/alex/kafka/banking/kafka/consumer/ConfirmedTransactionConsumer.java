package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import it.alex.kafka.banking.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumatore Kafka per ricevere eventi di transazioni confermate.
 */
@Component
@Slf4j
public class ConfirmedTransactionConsumer {

    /** Servizio per la gestione degli account */
    private final AccountService accountService;

    public ConfirmedTransactionConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    /** Ascolta il topic Kafka per le transazioni confermate.
     *
     * @param tx Evento di transazione confermata ricevuto
     */
    @KafkaListener(topics = "${app.topic.confirmed-transactions:confirmed-transactions}", groupId = "confirmed-group")
    public void listen(ConfirmedTransactionEvent tx) {
        if (tx == null) return;
        log.info("ConfirmedTransactionConsumer -> ricevuto txId={}", tx.getTransactionId());

        log.info("Saldo/i prima del trasferimento:");
        accountService.logAllBalances();

        String fromId = tx.getFromAccount().getAccountId();
        String toId = tx.getToAccount().getAccountId();
        boolean ok = accountService.applyTransfer(fromId, toId, tx.getAmount());
        if (!ok) {
            log.warn("ConfirmedTransactionConsumer -> trasferimento fallito per txId={}", tx.getTransactionId());
        }

        log.info("Dopo il trasferimento del saldo/i:");
        accountService.logAllBalances();
    }
}
