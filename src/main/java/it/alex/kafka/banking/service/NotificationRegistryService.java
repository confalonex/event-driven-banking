package it.alex.kafka.banking.service;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import it.alex.kafka.banking.model.NotificationTransactionEvent;

@Service
public class NotificationRegistryService {

    // txId -> NotificationTransactionEvent
    private final Map<String, NotificationTransactionEvent> registry = new ConcurrentHashMap<>();

    public void register(NotificationTransactionEvent n) {
        if (n == null || n.getTransactionId() == null) return;
        registry.put(n.getTransactionId(), n);
    }

    public NotificationTransactionEvent get(String transactionId) {
        return registry.get(transactionId);
    }

    public void markRead(String transactionId) {
        NotificationTransactionEvent n = registry.get(transactionId);
        if (n != null) {
            n.setStatus("READ");
            n.setReadAt(Instant.now());
            registry.put(transactionId, n);
        }
    }

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