package it.alex.kafka.banking;

import it.alex.kafka.banking.model.SecurityAlertEvent;
import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.KafkaProducerService;
import it.alex.kafka.banking.service.KafkaSecurityAlertProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Componente eseguito automaticamente all'avvio dell'applicazione.
 * Invia eventi Kafka di test (transazione e allerta sicurezza) e ne stampa lo storico ricevuto.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class StartupRunner implements CommandLineRunner {

    /**
     * Servizio per l'invio di eventi di transazione a Kafka.
     */
    private final KafkaProducerService producerService;

    /**
     * Servizio per l'invio di eventi di allerta sicurezza a Kafka.
     */
    private final KafkaSecurityAlertProducerService securityAlertProducerService;

    /**
     * Metodo eseguito al termine del bootstrap dell'applicazione Spring.
     * Crea e invia eventi su Kafka, quindi stampa lo storico in memoria.
     *
     * @param args argomenti della riga di comando (non utilizzati)
     */
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
        log.info("Evento transazione inviato: {}", transaction);

        // Evento di sicurezza
        SecurityAlertEvent alert = new SecurityAlertEvent(
                UUID.randomUUID().toString(),
                "ACC123456",
                "Accesso da IP sospetto",
                LocalDateTime.now()
        );
        securityAlertProducerService.sendAlert(alert);
        log.info("Evento sicurezza inviato: {}", alert);
    }
}
