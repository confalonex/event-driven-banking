package it.alex.kafka.banking.service;

import it.alex.kafka.banking.model.SecurityAlertEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile della ricezione e gestione degli eventi di allerta sicurezza da Kafka.
 */
@Service
@RequiredArgsConstructor
public class KafkaSecurityAlertConsumerService {

    /**
     * Riceve eventi di allerta sicurezza dal topic "security-alerts".
     *
     * @param alert Evento ricevuto da Kafka
     */
    @KafkaListener(
            topics = "security-alerts",
            groupId = "banking-group",
            containerFactory = "securityAlertListenerContainerFactory"
    )
    public void consumeAlert(SecurityAlertEvent alert) {
        System.out.println("Ricevuto evento di sicurezza: " + alert);
    }
}
