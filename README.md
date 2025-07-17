# Event‑Driven Banking

> **Architettura event‑driven con Apache Kafka, Kafka Streams e Spring Boot**

## Introduzione

Questo progetto dimostra come realizzare un microservizio bancario *event‑driven* capace di gestire transazioni e alert di sicurezza **in tempo reale**.

* **Spring Boot 3.5** – microservizio standalone.
* **Apache Kafka** – broker di messaggi distribuito.
* **Kafka Streams** – filtraggio e trasformazioni sullo stream di eventi.
* **Spring Cloud Stream** – consumer reattivi e scalabili.
* **Docker Compose** – avvio locale di Kafka (modalità **KRaft**, **senza ZooKeeper**).

## Funzionalità principali

| Evento               | Producer                            | Topic             | Stream Processing                                            | Consumer finale                                        |
| -------------------- | ----------------------------------- | ----------------- | ------------------------------------------------------------ | ------------------------------------------------------ |
| `TransactionEvent`   | `KafkaProducerService`              | `transactions`    | Topologia `KafkaStreamsTopology` filtra transazioni ≥ 1000 € | `HighValueTransactionConsumer` via Spring Cloud Stream |
| `SecurityAlertEvent` | `KafkaSecurityAlertProducerService` | `security-alerts` | —                                                            | `KafkaSecurityAlertConsumerService` via Spring Kafka   |

### Flusso semplificato

```
TransactionEvent  ──▶  transactions  ──▶  [Kafka Streams]  ──▶  high-value-transactions  ──▶  Cloud Stream Consumer
SecurityAlertEvent ─▶  security-alerts ─▶  Spring Kafka Listener
```

## Avvio rapido

1. **Prerequisiti**: Java 17, Maven 3, Docker.
2. **Avvia Kafka** (container KRaft):

   ```bash
   docker-compose up -d
   ```
3. **Avvia il microservizio**:

   ```bash
   ./mvnw spring-boot:run
   ```

   All’avvio `StartupRunner` pubblica alcuni eventi di esempio; osserva i log per verificarne l’elaborazione.

## Configurazione

Le proprietà principali sono in `src/main/resources/application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer: …
  cloud:
    stream:
      bindings:
        highValueTransactions-in-0:
          destination: high-value-transactions
```

Modifica `bootstrap-servers` se l’istanza Kafka risiede altrove.

## Struttura del progetto

```
src/
  main/java/it/alex/kafka/banking/              # codice applicativo
    config/                                     # configurazioni Kafka/KStreams
    model/                                      # DTO degli eventi
    service/                                    # producer & consumer Spring Kafka/Cloud Stream
    streams/                                    # topologie Kafka Streams
  test/java/…                                   # test unitari e d’integrazione
  resources/                                    # application.yml, logback, …
```

## Eseguire i test

```bash
./mvnw test
```

* **Kafka Streams Topology Test**: verifica il filtro delle transazioni a valore elevato.
* **Spring Context Test**: assicura che l’applicazione si avvii correttamente.

## Resilienza e scalabilità

* **Idempotenza**: duplicati gestiti tramite `KTable` keyed by `transactionId`.
* **Fault‑tolerance**: log Kafka replicato; state‑store RocksDB gestito da Kafka Streams.
* **Scalabilità**: consumer Cloud Stream in più istanze tramite *consumer groups*.

## Contribuire

1. Fork → branch feature → PR.
2. Scrivere test e documentazione per ogni nuova funzionalità.

## Licenza

Distribuito sotto **MIT License**; vedi `LICENSE`. 
