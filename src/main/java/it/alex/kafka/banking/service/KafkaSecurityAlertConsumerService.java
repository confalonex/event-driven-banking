package it.alex.kafka.banking.service;

import it.alex.kafka.banking.config.Topics;
import it.alex.kafka.banking.model.SecurityAlertEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Servizio responsabile della ricezione e gestione degli eventi di allerta sicurezza da Kafka.
 */
@Service
@Slf4j
public class KafkaSecurityAlertConsumerService {

    /**
     * Riceve eventi di allerta sicurezza dal topic "security-alerts".
     *
     * @param alert Evento ricevuto da Kafka
     */
    @KafkaListener(topics = Topics.SECURITY_ALERTS)
    public void consumeAlert(SecurityAlertEvent alert) {
        log.info("Ricevuto evento di sicurezza: {}", alert);
    }
}
