package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.SecurityAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile dell'invio di eventi di allerta sicurezza al topic Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSecurityAlertProducerService {

    private final KafkaTemplate<String, SecurityAlertEvent> kafkaTemplate;

    private static final String TOPIC_NAME = "security-alerts";

    /**
     * Invia un evento di sicurezza al topic Kafka.
     *
     * @param alert Evento di sicurezza da inviare
     */
    public void sendAlert(SecurityAlertEvent alert) {
        log.info("Invio evento di sicurezza Kafka: {}", alert);
        kafkaTemplate.send(TOPIC_NAME, alert.getAlertId(), alert);
    }
}
