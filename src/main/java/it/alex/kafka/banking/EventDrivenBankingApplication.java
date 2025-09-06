package it.alex.kafka.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe principale dell'applicazione Spring Boot.
 * Avvia il contesto e inizializza tutti i componenti.
 */
@SpringBootApplication
@EnableScheduling
public class EventDrivenBankingApplication {

	/**
	 * Metodo di ingresso principale dell'applicazione.
	 * Esegue l'avvio del contesto Spring.
	 *
	 * @param args argomenti da linea di comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(EventDrivenBankingApplication.class, args);
	}
}