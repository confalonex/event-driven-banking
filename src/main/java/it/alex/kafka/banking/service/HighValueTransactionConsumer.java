package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Consumer di Spring Cloud Stream che gestisce le transazioni di alto valore emesse dalla topologia di Kafka Streams.
 */
@Configuration
public class HighValueTransactionConsumer {

    /**
     * Esegue il log delle transazioni di alto valore in arrivo.
     * L'utilizzo di Spring Cloud Stream consente di scalare facilmente il consumer eseguendo più istanze nello stesso gruppo di consumer.
     */
    @Bean
    public Consumer<TransactionEvent> highValueConsumer() {
        return event -> System.out.println("[CloudStream] High value transaction: " + event);
    }
}
