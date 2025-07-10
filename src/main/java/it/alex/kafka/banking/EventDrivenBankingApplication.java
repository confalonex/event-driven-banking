package it.alex.kafka.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale dell'applicazione Spring Boot.
 * Avvia il contesto e inizializza tutti i componenti.
 */
@SpringBootApplication
public class EventDrivenBankingApplication {

	/**
	 * Metodo di ingresso principale dell'applicazione.
	 * Esegue l'avvio del contesto Spring.
	 *
	 * @param args argomenti da linea di comando (non usati)
	 */
	public static void main(String[] args) {
		SpringApplication.run(EventDrivenBankingApplication.class, args);
	}
}
