package it.alex.kafka.banking.service;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.NotificationTransactionEvent;

/**
 * Servizio per la registrazione e gestione degli eventi di notifica.<br>
 * Mantiene una mappa in memoria degli eventi di notifica associati alle transazioni.
 */
@Service
public class NotificationRegistryService {

    /** Mappa concorrente per memorizzare gli eventi di notifica per ID transazione */
    private final Map<String, NotificationTransactionEvent> registry = new ConcurrentHashMap<>();

    /** Registra un evento di notifica nella mappa */
    public void register(NotificationTransactionEvent n) {
        if (n == null || n.getTransactionId() == null) return;
        registry.put(n.getTransactionId(), n);
    }

    /** Recupera un evento di notifica dalla mappa tramite l'ID transazione */
    public NotificationTransactionEvent get(String transactionId) {
        return registry.get(transactionId);
    }

    /** Segna un evento di notifica come letto aggiornandone lo stato e il timestamp */
    public void markRead(String transactionId) {
        NotificationTransactionEvent n = registry.get(transactionId);
        if (n != null) {
            n.setStatus("READ");
            n.setReadAt(Instant.now());
            registry.put(transactionId, n);
        }
    }

    /** Trova tutte le transazioni con notifiche inviate da più di 'seconds' secondi */
    public List<String> findSentOlderThanSeconds(long seconds) {
        List<String> out = new ArrayList<>();
        Instant cutoff = Instant.now().minus(Duration.ofSeconds(seconds));
        for (Map.Entry<String, NotificationTransactionEvent> e : registry.entrySet()) {
            NotificationTransactionEvent n = e.getValue();
            if ("SENT".equals(n.getStatus()) && n.getSentAt() != null && n.getSentAt().isBefore(cutoff)) {
                out.add(e.getKey());
            }
        }
        return out;
    }
}