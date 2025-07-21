# Event‑Driven Banking

Applicazione demo che simula una piattaforma bancaria basata su eventi in tempo reale con **Spring Boot** e **Apache Kafka**.

## Caratteristiche

* Pipeline asincrona su Kafka con i topic **transactions**, **security‑alerts** e **high‑value‑transactions**.
* Filtraggio e deduplicazione in streaming con Kafka Streams.
* Consumer sia nativi (Spring Kafka) che dichiarativi (Spring Cloud Stream).
* Avvio one‑command via Docker‑Compose (Kafka in modalità **KRaft**).
* Strategie di resilienza già integrate (state store, offset, idempotenza).

## Stack Tecnologico

| Livello           | Tecnologia                | Note      |
| ----------------- | ------------------------- | --------- |
| Framework         | Spring Boot 3.5 · Java 17 |           |
| Messaggistica     | Apache Kafka 3.x (KRaft)  | Immagine `bitnami/kafka` |
| Stream processing | Kafka Streams             | Integrato nell'app |
| Abstraction layer | Spring Cloud Stream       | Binder Kafka |
| Build             | Maven                     |           |

## Prerequisiti

* **Java 17+**
* **Maven 3.9+**
* **Docker**

## Avvio rapido

```bash
# Avvia Kafka (modalità detached)
docker compose up -d

# Compila ed esegui l'app Spring Boot
./mvnw spring-boot:run
```

`StartupRunner` profilo impostato su **dev** produce subito un `TransactionEvent` con amount pari a 1050 € e un `SecurityAlertEvent` per verificare la pipeline.

Stop servizi:

```bash
docker compose down -v
```

## Struttura del progetto

```
src/main/java/it/alex/kafka/banking
 ├── config      # Costanti topic
 ├── model       # DTO degli eventi
 ├── service     # Producer & consumer
 └── streams     # Topologia Kafka Streams
```

## Flusso degli eventi

Il ciclo completo è composto da **tre fasi**.

1. **Produzione**

   * `KafkaProducerService` → topic **transactions**
   * `KafkaSecurityAlertProducerService` → topic **security‑alerts**

2. **Elaborazione in streaming**

   * `KafkaStreamsTopology` legge da **transactions**
   * Filtra gli importi ≥ 1000 €
   * Deduplica via `KTable` su `transactionId`
   * Pubblica su **high‑value‑transactions**

3. **Consumo**

   * `KafkaConsumerService` → elabora tutte le transazioni
   * `HighValueTransactionConsumer` → elabora solo quelle ad alto valore
   * `KafkaSecurityAlertConsumerService` → gestisce gli alert

```
TransactionProducer --> (transactions) --> StreamsFilter --> (high-value-transactions) --> HighValueConsumer
SecurityProducer    --> (security-alerts)  -------------------------------> SecurityAlertConsumer
                                          \                            
                                           +--> AllTransactionConsumer
```

*Le frecce rappresentano i topic Kafka; i blocchi sono classi Spring.*

## Configurazione principale (`application.yml`)

| Chiave                                                            | Valore default          | Descrizione           |
| ----------------------------------------------------------------- | ----------------------- | --------------------- |
| `spring.kafka.bootstrap-servers`                                  | `localhost:9092`        | Broker Kafka          |
| `spring.kafka.streams.properties.cleanup.on.start`                | `true`              | Reset store all'avvio |
| `spring.cloud.stream.bindings.highValueConsumer-in-0.destination` | `high-value-transactions` | Binding Cloud Stream  |

## Test

```bash
./mvnw test
```

Test di unità e di topologia in `src/test`.

## Strategie di resilienza

* **Persistenza**: log Kafka, nessuna dipendenza da ZooKeeper.
* **Idempotenza**: `KTable` chiave `transactionId` elimina duplicati.
* **Offset** gestiti automaticamente dai consumer.

## Licenza

MIT. Vedi `LICENSE`.
