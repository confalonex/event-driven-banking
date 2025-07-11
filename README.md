# Event Driven Banking

Questo progetto dimostra un'architettura **event-driven** basata su Apache Kafka per la generazione e l'elaborazione di eventi in tempo reale. L'applicazione sfrutta Spring Boot insieme a **Kafka Streams** e **Spring Cloud Stream** per mostrare come implementare producer, consumer e flussi di elaborazione con resilienza e scalabilità.

## Architettura

- **Produttori Kafka**: inviano eventi di `TransactionEvent` e `SecurityAlertEvent` verso i relativi topic.
- **Consumatori Kafka**: ricevono gli eventi e li elaborano in tempo reale.
- **Kafka Streams**: un topology dedicata filtra le transazioni con importo elevato e le pubblica sul topic `high-value-transactions` applicando una logica di idempotenza tramite `KTable`.
- **Spring Cloud Stream**: un `Consumer` gestisce gli eventi provenienti dal topic `high-value-transactions`, permettendo di scalare orizzontalmente il servizio grazie al meccanismo di gruppi di consumer.

Di seguito uno schema semplificato:

```
TransactionEvent ----> [transactions topic] ----> Kafka Streams ---> [high-value-transactions topic] ---> Cloud Stream Consumer
SecurityAlertEvent --> [security-alerts topic] --> Kafka Listener
```

## Configurazione

Le principali impostazioni si trovano in `application.yml`:

- `spring.kafka` definisce i parametri base di broker, producer e consumer.
- `spring.cloud.stream` configura il binding del consumer Spring Cloud Stream verso `high-value-transactions`.

Nel file `docker-compose.yml` sono disponibili i container di **Kafka** e **Zookeeper** per l'esecuzione locale.

## Resilienza e idempotenza

- Il topology Kafka Streams utilizza un `KTable` per memorizzare le transazioni in base al loro `transactionId`. Questo meccanismo assicura che eventi duplicati vengano sovrascritti, fornendo idempotenza.
- Kafka garantisce la replica dei dati sui topic e, in combinazione con Kafka Streams, assicura la ripartenza in caso di fault (state store su Kafka changelog topic).
- I consumer Spring Cloud Stream possono essere scalati su più istanze appartenenti allo stesso gruppo, sfruttando la partizionamento dei topic per la scalabilità.

## Avvio del progetto

1. Avviare Kafka tramite Docker Compose:

```bash
docker-compose up -d
```

2. Avviare l'applicazione Spring Boot:

```bash
./mvnw spring-boot:run
```

Durante l'avvio, lo `StartupRunner` invierà alcuni eventi di test. Nel log verranno mostrati i messaggi elaborati dalla topologia Kafka Streams e dal consumer Spring Cloud Stream.

## Test

Il progetto include un semplice test di caricamento del contesto e un test per la topologia Kafka Streams (`HighValueTransactionsTopologyTest`). Eseguire:

```bash
./mvnw test
```

