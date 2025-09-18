package it.alex.kafka.banking;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import it.alex.kafka.banking.kafka.producer.InitiatedTransactionProducer;
import it.alex.kafka.banking.model.Account;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.AccountService;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente che esegue operazioni all'avvio dell'applicazione.<br>
 * Simula la creazione di account e l'invio di transazioni iniziate.
 */
@Component
@Slf4j
public class StartupRunner implements CommandLineRunner {

    /** Produttore Kafka per le transazioni iniziate */
    private final InitiatedTransactionProducer initiatedProducer;

    /** Servizio per la gestione degli account */
    private final AccountService accountService;

    public StartupRunner(InitiatedTransactionProducer initiatedProducer, AccountService accountService) {
        this.initiatedProducer = initiatedProducer;
        this.accountService = accountService;
    }

    /**
     * Metodo eseguito all'avvio dell'applicazione per simulare la creazione di account
     * e l'invio di transazioni iniziate.
     *
     * @param args Argomenti della riga di comando
     */
    @Override
    public void run(String... args) {
        for (int i = 1; i <= 1; i++) {
            String txId = UUID.randomUUID().toString();

            Account from = new Account(
                    "acc-from-" + i,
                    "Alice-" + i,
                    new BigDecimal("1000.00"));

            log.info("StartupRunner -> da account: {}", from);

            Account to = new Account(
                    "acc-to-" + i,
                    "Bob-" + i,
                    new BigDecimal("100.00"));

            log.info("StartupRunner -> a account: {}", to);

            // registra gli account in memoria
            accountService.register(from);
            accountService.register(to);

            TransactionEvent tx = new TransactionEvent(
                    txId,
                    from,
                    to,
                    new BigDecimal("50.00").multiply(BigDecimal.valueOf(i)),
                    Instant.now(),
                    "INITIATED"
            );

            log.info("StartupRunner -> inviando transazione inizializzata txId={}", txId);
            initiatedProducer.sendInitiated(tx);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            log.info("StartupRunner -> conti coinvolti: from={}, to={}", from, to);
            log.info("StartupRunner -> transazione avviata inviata: {}", tx);
        }
    }
}
