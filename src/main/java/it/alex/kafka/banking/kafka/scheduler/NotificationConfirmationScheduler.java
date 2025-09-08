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

/**
 * Scheduler che periodicamente controlla le notifiche inviate e conferma
 * quelle che non sono state lette entro una soglia di tempo specificata.<br><br>
 * Utilizza il servizio NotificationRegistryService per recuperare le notifiche
 * e il ConfirmedTransactionProducer per inviare gli eventi di conferma.<br><br>
 * La soglia di tempo può essere configurata tramite la proprietà
 * "app.confirm.threshold.seconds".
 * Il delay tra le esecuzioni del controllo può essere configurato tramite la
 * proprietà "app.confirm.delay.ms".
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConfirmationScheduler {

    /** Servizio per la gestione delle notifiche */
    private final NotificationRegistryService registry;

    /** Produttore per gli eventi di transazione confermata */
    private final ConfirmedTransactionProducer confirmedProducer;

    /** Soglia di tempo in secondi per considerare una notifica come non letta */
    @Value("${app.confirm.threshold.seconds:5}")
    private long thresholdSeconds;

    /**
     * Metodo schedulato che controlla periodicamente le notifiche inviate
     * e conferma quelle che non sono state lette entro la soglia di tempo
     * specificata.<br><br>
     * Viene eseguito ogni "app.confirm.delay.ms" millisecondi.
     */
    @Scheduled(fixedDelayString = "${app.confirm.delay.ms:2000}")
    public void checkAndConfirm() {
        List<String> toConfirm = registry.findSentOlderThanSeconds(thresholdSeconds);
        if (toConfirm.isEmpty()) return;
        log.info("Scheduler -> trovate {} notifiche da confermare", toConfirm.size());
        // per ogni notifica da confermare costruisci e invia l'evento di conferma
        for (String txId : toConfirm) {
            try {
                NotificationTransactionEvent n = registry.get(txId);
                if (n == null) continue;

                // marca come letta nella registry
                registry.markRead(txId);

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
                log.error("Scheduler -> confermato errore txId={}", txId, ex);
            }
        }
    }
}
