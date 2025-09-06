package it.alex.kafka.banking.kafka.producer;

import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatedInitiatedTransactionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.topic.validated-transactions:validated-transactions}")
    private String validatedTopic;

    public void sendValidated(ValidatedTransactionEvent tx) {
        if (tx == null || tx.getTransactionId() == null) return;
        log.info("ValidatedInitiatedTransactionProducer -> send txId={} valid={}", tx.getTransactionId(), tx.isValid());
        kafkaTemplate.send(validatedTopic, tx.getTransactionId(), tx);
    }
}
