package it.alex.kafka.banking.runner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import it.alex.kafka.banking.kafka.producer.InitiatedTransactionProducer;
import it.alex.kafka.banking.model.Account;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente che esegue operazioni all'avvio dell'applicazione.<br>
 * Simula la creazione di account e l'invio di transazioni iniziate.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StartupRunner implements CommandLineRunner {

    /** Produttore Kafka per le transazioni iniziate */
    private final InitiatedTransactionProducer initiatedProducer;

    /** Servizio per la gestione degli account */
    private final AccountService accountService;

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

            Account from = Account.builder()
                    .accountId("acc-from-" + i)
                    .owner("Alice-" + i)
                    .balance(new BigDecimal("1000.00"))
                    .build();

            log.info("StartupRunner -> da account: {}", from);

            Account to = Account.builder()
                    .accountId("acc-to-" + i)
                    .owner("Bob-" + i)
                    .balance(new BigDecimal("100.00"))
                    .build();

            log.info("StartupRunner -> a account: {}", to);

            // registra gli account in memoria
            accountService.register(from);
            accountService.register(to);

            TransactionEvent tx = TransactionEvent.builder()
                    .transactionId(txId)
                    .fromAccount(from)
                    .toAccount(to)
                    .amount(new BigDecimal("50.00").multiply(BigDecimal.valueOf(i)))
                    .createdAt(Instant.now())
                    .status("INITIATED")
                    .build();

            log.info("StartupRunner -> inviando transazione inizializzata txId={}", txId);
            initiatedProducer.sendInitiated(tx);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            log.info("StartupRunner -> involved accounts: from={}, to={}", from, to);
            log.info("StartupRunner -> sent initiated transaction: {}", tx);
        }
    }
}
