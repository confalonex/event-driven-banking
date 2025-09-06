package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitiatedTransactionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topic.transactions:transactions}")
    private String transactionsTopic;

    public void sendInitiated(TransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("InitiatedTransactionProducer -> send txId={}", tx.getTransactionId());
        kafkaTemplate.send(transactionsTopic, tx.getTransactionId(), tx);
    }
}
