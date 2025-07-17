package it.alex.kafka.banking.streams;

import it.alex.kafka.banking.model.TransactionEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class KafkaStreamsTopologyTest {

    private TopologyTestDriver driver;
    private TestInputTopic<String, TransactionEvent> input;
    private TestOutputTopic<String, TransactionEvent> output;

    @BeforeEach
    void setup() {
        StreamsBuilder builder = new StreamsBuilder();
        new KafkaStreamsTopology().highValueTransactionsTopology(builder);
        Topology topology = builder.build();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "it.alex.kafka.banking.model");
        driver = new TopologyTestDriver(topology, props);

        JsonSerde<TransactionEvent> serde = new JsonSerde<>(TransactionEvent.class);
        input = driver.createInputTopic(
                "transactions",
                Serdes.String().serializer(),
                serde.serializer()
        );
        output = driver.createOutputTopic(
                "high-value-transactions",
                Serdes.String().deserializer(),
                serde.deserializer()
        );
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }

    @Test
    void whenHighValue_thenOutput() {
        TransactionEvent event = new TransactionEvent(
                "tx1",
                "account123",
                1500.0,
                "deposit",
                LocalDateTime.now()
        );

        input.pipeInput(event.getTransactionId(), event);

        assertFalse(output.isEmpty(), "Il topic di alto valore non deve essere vuoto");
        assertEquals(1, output.readRecordsToList().size()); // Verifica che ci sia un record
    }

    @Test
    void whenLowValue_thenNoOutput() {
        TransactionEvent event = new TransactionEvent(
                "tx2",
                "account456",
                500.0,
                "withdrawal",
                LocalDateTime.now()
        );

        input.pipeInput(event.getTransactionId(), event); // Invia un evento di basso valore

        assertTrue(output.isEmpty(), "Il topic di alto valore deve restare vuoto per importi bassi");
    }
}
