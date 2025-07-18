package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Consumer di Spring Cloud Stream che gestisce le transazioni di alto valore emesse dalla topologia di Kafka Streams.
 */
@Configuration
@Slf4j
public class HighValueTransactionConsumer {

    /**
     * Esegue il log delle transazioni di alto valore in arrivo.
     */
    @Bean
    public Consumer<TransactionEvent> highValueConsumer() {
        return event -> {
            log.info("[CloudStream] High value transaction: {}", event);
        };
    }
}
