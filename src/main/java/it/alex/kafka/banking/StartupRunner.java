package it.alex.kafka.banking;

import it.alex.kafka.banking.model.SecurityAlertEvent;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.KafkaProducerService;
import it.alex.kafka.banking.service.KafkaSecurityAlertProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Invia eventi Kafka di test all'avvio dell'applicazione.
 */
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final KafkaProducerService producerService;
    private final KafkaSecurityAlertProducerService securityAlertProducerService;

    @Override
    public void run(String... args) {
        // Evento di transazione
        TransactionEvent transaction = new TransactionEvent(
                UUID.randomUUID().toString(),
                "ACC123456",
                150.0,
                "deposit",
                LocalDateTime.now()
        );
        producerService.sendTransaction(transaction);

        // Evento di sicurezza
        SecurityAlertEvent alert = new SecurityAlertEvent(
                UUID.randomUUID().toString(),
                "ACC123456",
                "Accesso da IP sospetto",
                LocalDateTime.now()
        );
        securityAlertProducerService.sendAlert(alert);
    }
}
