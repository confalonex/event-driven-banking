package it.alex.kafka.banking.kafka.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.alex.kafka.banking.kafka.producer.ConfirmedTransactionProducer;
import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import it.alex.kafka.banking.model.NotificationTransactionEvent;
import it.alex.kafka.banking.service.NotificationRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConfirmationScheduler {

    private final NotificationRegistryService registry;
    private final ConfirmedTransactionProducer confirmedProducer;

    @Value("${app.confirm.threshold.seconds:5}")
    private long thresholdSeconds;

    @Scheduled(fixedDelayString = "${app.confirm.delay.ms:2000}")
    public void checkAndConfirm() {
        List<String> toConfirm = registry.findSentOlderThanSeconds(thresholdSeconds);
        if (toConfirm.isEmpty()) return;
        log.info("Scheduler -> found {} notifications to confirm", toConfirm.size());
        for (String txId : toConfirm) {
            try {
                NotificationTransactionEvent n = registry.get(txId);
                if (n == null) continue;

                // marca come letta nella registry
                registry.markRead(txId);

                // costruisci e invia il ConfirmedTransactionEvent
                ConfirmedTransactionEvent c = ConfirmedTransactionEvent.builder()
                        .transactionId(n.getTransactionId())
                        .fromAccount(n.getFromAccount())
                        .toAccount(n.getToAccount())
                        .amount(n.getAmount())
                        .createdAt(n.getCreatedAt())
                        .status("CONFIRMED")
                        .valid(n.isValid())
                        .reason(n.getReason())
                        .validatedAt(n.getValidatedAt())
                        .confirmedAt(java.time.Instant.now())
                        .build();

                confirmedProducer.sendConfirmed(c);
            } catch (Exception ex) {
                log.error("Scheduler -> error confirming txId={}", txId, ex);
            }
        }
    }
}
