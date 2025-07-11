# Event Driven Banking

Questo progetto dimostra una semplice architettura **event-driven** costruita con Spring Boot e Kafka. Lo scopo è simulare l'invio e l'elaborazione in tempo reale di transazioni bancarie e alert di sicurezza.

## Contenuti
- **Produttori Kafka** per eventi `TransactionEvent` e `SecurityAlertEvent`.
- **Consumatori Kafka** per la gestione di questi eventi.
- **Kafka Streams** per filtrare le transazioni di alto valore garantendo idempotenza con una `KTable`.
- **Spring Cloud Stream** per consumare in modo scalabile gli eventi filtrati.

## Architettura
```
TransactionEvent ----> [transactions topic] ----> Kafka Streams ---> [high-value-transactions topic] ---> Cloud Stream Consumer
SecurityAlertEvent --> [security-alerts topic] --> Kafka Listener
```
La topologia Kafka Streams legge dal topic `transactions`, rimuove i duplicati grazie a una `KTable` e inoltra solo le transazioni con importo ≥ `1000` al topic `high-value-transactions`. Un consumer Spring Cloud Stream (bean `highValueConsumer`) si occupa di elaborare questi eventi.

## Configurazione
Le impostazioni sono definite in [`application.yml`](src/main/resources/application.yml):
- sezione `spring.kafka` per producer e consumer.
- sezione `spring.cloud.stream` per il binding del consumer Cloud Stream.

Per eseguire Kafka in locale è presente un [`docker-compose.yml`](docker-compose.yml) che avvia Zookeeper e un broker Kafka.

## Avvio dell’applicazione
1. Avviare Kafka tramite Docker Compose:
   ```bash
   docker-compose up -d
   ```
2. Avviare l'applicazione Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```
All’avvio lo `StartupRunner` invierà alcuni eventi di esempio e nel log saranno visibili i messaggi prodotti dalla topologia Kafka Streams e dal consumer Spring Cloud Stream.

## Test
Per eseguire i test unitari (inclusa la verifica della topologia Kafka Streams) lanciare:
```bash
./mvnw test
```

## Requisiti
- Java 17
- Maven 3
- Docker (per eseguire Kafka in locale)

## Riferimenti
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Kafka](https://kafka.apache.org/)
- [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)
- [Kafka Streams](https://kafka.apache.org/documentation/streams/)

