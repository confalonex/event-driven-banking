package it.alex.kafka.banking.kafka.consumer;

import it.alex.kafka.banking.model.ValidatedTransactionEvent;
import it.alex.kafka.banking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationTransactionConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${app.topic.validated-transactions:validated-transactions}", groupId = "notification-group")
    public void listen(ValidatedTransactionEvent tx) {
        log.info("NotificationTransactionConsumer -> received txId={} valid={}", tx == null ? "null" : tx.getTransactionId(), tx == null ? false : tx.isValid());
        notificationService.notifyUser(tx);
    }
}
