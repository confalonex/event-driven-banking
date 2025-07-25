package it.alex.kafka.banking.streams;

import it.alex.kafka.banking.model.TransactionEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerde;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test per la topologia Kafka Streams che filtra le transazioni di alto valore.
 */
class HighValueTransactionsTopologyTest {

    /**
     * Test che verifica il corretto funzionamento della topologia Kafka Streams per filtrare le transazioni di alto valore.
     */
    @Test
    void testFiltering() {
        KafkaStreamsTopology config = new KafkaStreamsTopology();
        StreamsBuilder builder = new StreamsBuilder();
        config.highValueTransactionsTopology(builder);
        Topology topology = builder.build();

        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            JsonSerde<TransactionEvent> serde = new JsonSerde<>(TransactionEvent.class);
            TestInputTopic<String, TransactionEvent> input = driver.createInputTopic(
                    "transactions", Serdes.String().serializer(), serde.serializer());
            TestOutputTopic<String, TransactionEvent> output = driver.createOutputTopic(
                    "high-value-transactions", Serdes.String().deserializer(), serde.deserializer());

            // invia una transazione di basso valore
            TransactionEvent low = new TransactionEvent(UUID.randomUUID().toString(),
                    "A1", 100, "deposit", LocalDateTime.now());
            input.pipeInput(low.getTransactionId(), low);

            // invia una transazione di alto valore
            TransactionEvent high = new TransactionEvent(UUID.randomUUID().toString(),
                    "A1", 1500, "deposit", LocalDateTime.now());
            input.pipeInput(high.getTransactionId(), high);

            List<TestRecord<String, TransactionEvent>> results = output.readRecordsToList();
            assertThat(results).hasSize(1);
            assertThat(results.get(0).value().getAmount()).isEqualTo(1500);
        }
    }
}
