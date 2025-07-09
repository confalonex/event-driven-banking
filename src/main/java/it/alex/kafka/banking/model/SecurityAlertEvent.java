package it.alex.kafka.banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Rappresenta un evento di allerta di sicurezza su un conto bancario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAlertEvent {

    /** Identificativo univoco dell’allerta */
    private String alertId;

    /** ID del conto coinvolto */
    private String accountId;

    /** Messaggio dell’allerta */
    private String message;

    /** Data e ora della generazione dell’allerta */
    private LocalDateTime timestamp;
}
