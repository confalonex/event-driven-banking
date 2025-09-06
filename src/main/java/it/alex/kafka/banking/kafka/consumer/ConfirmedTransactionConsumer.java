package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import it.alex.kafka.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmedTransactionConsumer {

    private final AccountService accountService;

    @KafkaListener(topics = "${app.topic.confirmed-transactions:confirmed-transactions}", groupId = "confirmed-group")
    public void listen(ConfirmedTransactionEvent tx) {
        if (tx == null) return;
        log.info("ConfirmedTransactionConsumer -> received txId={}", tx.getTransactionId());

        log.info("Before transfer balances:");
        accountService.logAllBalances();

        String fromId = tx.getFromAccount().getAccountId();
        String toId = tx.getToAccount().getAccountId();
        boolean ok = accountService.applyTransfer(fromId, toId, tx.getAmount());
        if (!ok) {
            log.warn("ConfirmedTransactionConsumer -> transfer failed for txId={}", tx.getTransactionId());
        }

        log.info("After transfer balances:");
        accountService.logAllBalances();
    }
}
