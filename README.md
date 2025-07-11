# Event Driven Banking

Questo progetto dimostra un'architettura **event-driven** basata su **Apache Kafka**, implementata con **Spring Boot**, **Kafka Streams** e **Spring Cloud Stream** per simulare l'invio e l'elaborazione in tempo reale di transazioni bancarie e alert di sicurezza.

## Funzionalità principali

* **Produttori Kafka** per eventi:

    * `TransactionEvent`
    * `SecurityAlertEvent`

* **Consumatori Kafka** che elaborano gli eventi in tempo reale.

* **Kafka Streams**:

    * Topology per filtrare transazioni di valore elevato (≥ 1000€).
    * Idempotenza garantita tramite `KTable`.

* **Spring Cloud Stream**:

    * Consumer scalabile per gli eventi filtrati sul topic `high-value-transactions`.

## Schema architetturale semplificato

```
TransactionEvent ----> [transactions topic] ----> Kafka Streams ---> [high-value-transactions topic] ---> Cloud Stream Consumer
SecurityAlertEvent --> [security-alerts topic] --> Kafka Listener
```

## Configurazione

La configurazione è gestita tramite [`application.yml`](src/main/resources/application.yml):

* **Kafka**: parametri di connessione per producer e consumer (`spring.kafka`).
* **Spring Cloud Stream**: binding per il consumer scalabile (`spring.cloud.stream`).

Per eseguire **Kafka** e **Zookeeper** localmente, utilizzare il file [`docker-compose.yml`](docker-compose.yml).

## Resilienza e scalabilità

* Idempotenza con `KTable` per gestire duplicati basati su `transactionId`.
* Repliche Kafka garantiscono persistenza e fault-tolerance.
* Consumer scalabili grazie a gruppi consumer Spring Cloud Stream.

## Avvio rapido

### 1. Avvia Kafka con Docker

```bash
docker-compose up -d
```

### 2. Esegui l'applicazione

```bash
./mvnw spring-boot:run
```

All'avvio, lo `StartupRunner` invierà automaticamente eventi di esempio. Controlla i log per verificare l'elaborazione.

## Eseguire i test

Il progetto contiene test unitari e di integrazione per la verifica della topologia Kafka Streams:

```bash
./mvnw test
```

## Requisiti

* Java 17
* Maven 3
* Docker
