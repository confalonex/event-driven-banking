package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.SecurityAlertEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile dell'invio di eventi di allerta sicurezza al topic Kafka.
 */
@Service
@RequiredArgsConstructor
public class KafkaSecurityAlertProducerService {

    /**
     * Template Kafka per l'invio di eventi di allerta sicurezza.
     * Utilizza una chiave di tipo String e un valore di tipo SecurityAlertEvent.
     */
    private final KafkaTemplate<String, SecurityAlertEvent> securityAlertKafkaTemplate;

    /**
     * Nome del topic Kafka per eventi di sicurezza.
     */
    private static final String TOPIC_NAME = "security-alerts";

    /**
     * Invia un evento di sicurezza al topic Kafka.
     *
     * @param alert evento di allerta sicurezza da inviare
     */
    public void sendAlert(SecurityAlertEvent alert) {
        securityAlertKafkaTemplate.send(TOPIC_NAME, alert.getAlertId(), alert);
    }
}
