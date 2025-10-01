package it.alex.kafka.esercizi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale dell'applicazione Spring Boot.<br>
 * Avvia il contesto e inizializza tutti i componenti.
 */
@SpringBootApplication
public class StartApplication {

	/**
	 * Metodo di ingresso principale dell'applicazione.<br>
	 * Esegue l'avvio del contesto Spring.
	 *
	 * @param args argomenti da linea di comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}
}