package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.TransactionEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Spring Cloud Stream consumer that handles high value transactions emitted by
 * the Kafka Streams topology.
 */
@Configuration
public class HighValueTransactionConsumer {

    /**
     * Logs incoming high value transactions. Using Spring Cloud Stream allows
     * the consumer to be easily scaled by running multiple instances in the
     * same consumer group.
     */
    @Bean
    public Consumer<TransactionEvent> highValueConsumer() {
        return event -> System.out.println("[CloudStream] High value transaction: " + event);
    }
}
