package it.alex.kafka.banking.streams;

import it.alex.kafka.banking.model.TransactionEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

/**
 * Defines a Kafka Streams topology that filters high value transactions and
 * outputs them to a dedicated topic. Duplicate transaction IDs are collapsed
 * via a KTable providing a simple idempotency mechanism.
 */
@EnableKafkaStreams
@Configuration
public class KafkaStreamsTopology {

    /**
     * Builds the Kafka Streams topology. Events from the {@code transactions}
     * topic are materialized into a KTable to remove duplicates. Transactions
     * with amount greater than or equal to 1000 are forwarded to the
     * {@code high-value-transactions} topic.
     */
    @Bean
    public KStream<String, TransactionEvent> highValueTransactionsTopology(StreamsBuilder builder) {
        JsonSerde<TransactionEvent> serde = new JsonSerde<>(TransactionEvent.class);

        // Read stream from 'transactions' topic
        KStream<String, TransactionEvent> input = builder.stream("transactions",
                Consumed.with(Serdes.String(), serde));

        // Materialize as table to achieve idempotency on transactionId
        KTable<String, TransactionEvent> table = input.groupByKey()
                .reduce((agg, val) -> val, Materialized.with(Serdes.String(), serde));

        // Filter for high value transactions and send to dedicated topic
        table.toStream()
             .filter((key, value) -> value.getAmount() >= 1000)
             .to("high-value-transactions", Produced.with(Serdes.String(), serde));

        return input;
    }
}
