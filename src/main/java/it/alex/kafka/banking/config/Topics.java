package it.alex.kafka.banking.config;

/**
 * Costanti per i nomi dei topic Kafka usati nell'applicazione.
 */
public final class Topics {

    /** Topic per tutti gli eventi di transazione */
    public static final String TRANSACTIONS = "transactions";

    /** Topic per gli eventi di allerta di sicurezza */
    public static final String SECURITY_ALERTS = "security-alerts";

    /** Topic per le transazioni di alto valore (amount >= 1000) */
    public static final String HIGH_VALUE_TRANSACTIONS = "high-value-transactions";

    // Classe di utilità non istanziabile
    private Topics() { }
}
