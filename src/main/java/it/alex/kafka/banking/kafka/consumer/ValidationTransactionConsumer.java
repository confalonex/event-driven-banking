package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.TransactionEvent;
import it.alex.kafka.banking.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationTransactionConsumer {

    private final ValidationService validationService;

    @KafkaListener(topics = "${app.topic.transactions:transactions}", groupId = "validation-group")
    public void listen(TransactionEvent tx) {
        log.info("ValidationTransactionConsumer -> received txId={}", tx == null ? "null" : tx.getTransactionId());
        validationService.validate(tx);
    }
}
