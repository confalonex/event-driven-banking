package it.alex.kafka.banking;

import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe di avvio che invia un evento Kafka di test quando l'applicazione parte.
 */
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final KafkaProducerService producerService;

    @Override
    public void run(String... args) {
        TransactionEvent event = new TransactionEvent(
                UUID.randomUUID().toString(),     // transactionId
                "ACC123456",                      // accountId
                150.0,                            // amount
                "deposit",                        // type
                LocalDateTime.now()               // timestamp
        );

        producerService.sendTransaction(event);
    }
}
