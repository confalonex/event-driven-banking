package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.ConfirmedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmedTransactionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topic.confirmed-transactions:confirmed-transactions}")
    private String confirmedTopic;

    public void sendConfirmed(ConfirmedTransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("ConfirmedTransactionProducer -> send txId={}", tx.getTransactionId());
        kafkaTemplate.send(confirmedTopic, tx.getTransactionId(), tx);
    }
}
