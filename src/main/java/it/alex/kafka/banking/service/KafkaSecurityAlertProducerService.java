package it.alex.kafka.banking.service;

import it.alex.kafka.banking.config.Topics;
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
     * Invia un evento di sicurezza al topic Kafka.
     *
     * @param alert evento di allerta sicurezza da inviare
     */
    public void sendAlert(SecurityAlertEvent alert) {
        securityAlertKafkaTemplate.send(Topics.SECURITY_ALERTS, alert.getAlertId(), alert);
    }
}
