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
 * Definisce una topologia Kafka Streams che filtra le transazioni di alto valore e le invia a un topic dedicato.
 * Gli ID di transazione duplicati vengono gestiti tramite una KTable, fornendo un semplice meccanismo di idempotenza.
 */
@EnableKafkaStreams
@Configuration
public class KafkaStreamsTopology {

    /**
     * Costruisce la topologia Kafka Streams.
     * Gli eventi provenienti dal topic {@code transactions} vengono materializzati in una KTable per rimuovere i duplicati.
     * Le transazioni con importo maggiore o uguale a 1000 vengono inoltrate al topic {@code high-value-transactions}.
     */
    @Bean
    public KStream<String, TransactionEvent> highValueTransactionsTopology(StreamsBuilder builder) {
        JsonSerde<TransactionEvent> serde = new JsonSerde<>(TransactionEvent.class);

        // Legge gli eventi dal topic "transactions"
        KStream<String, TransactionEvent> input = builder.stream("transactions",
                Consumed.with(Serdes.String(), serde));

        // Materializza gli eventi in una KTable per rimuovere i duplicati
        KTable<String, TransactionEvent> table = input.groupByKey()
                .reduce((agg, val) -> val, Materialized.with(Serdes.String(), serde));

        // Filtra le transazioni di alto valore e le invia al topic "high-value-transactions"
        table.toStream()
             .filter((key, value) -> value.getAmount() >= 1000)
             .to("high-value-transactions", Produced.with(Serdes.String(), serde));

        return input;
    }
}
