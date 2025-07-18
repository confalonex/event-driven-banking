# Documentazione Event‑Driven Banking

---

## Introduzione

Questo microservizio bancario adotta un’architettura event‑driven per gestire transazioni e alert di sicurezza in tempo reale tramite Apache Kafka in modalità KRaft, Kafka Streams, Spring Boot 3.5 e Spring Cloud Stream. Tutti i componenti sono containerizzati con Docker Compose per garantire un setup locale riproducibile.

---

## Struttura del codice sorgente

```
└── it/alex/kafka/banking
    ├── EventDrivenBankingApplication.java   # Bootstrap Spring Boot
    ├── StartupRunner.java                   # Sender eventi all'avvio
    ├── config/
    │   └── Topics.java                      # Nomi costanti dei topic Kafka
    ├── model/
    │   ├── TransactionEvent.java            # Rappresentazione di un evento di transazione bancaria
    │   └── SecurityAlertEvent.java          # Rappresentazione di un evento di alert di sicurezza
    ├── service/
    │   ├── KafkaProducerService.java        # Producer transazioni
    │   ├── KafkaConsumerService.java        # Consumer transazioni
    │   ├── KafkaSecurityAlertProducerService.java   # Producer alert di sicurezza
    │   └── KafkaSecurityAlertConsumerService.java   # Consumer alert di sicurezza
    └── streams/
        ├── KafkaStreamsTopology.java        # Topologia filtro high‑value
        └── HighValueTransactionConsumer.java        # Consumer Cloud Stream
```

---

## Configurazione applicativa – `application.yml`

```yaml
spring:
  application:
    name: event-driven-banking # Nome dell'applicazione
  profiles:
    active: dev # Profilo attivo per l'ambiente di sviluppo

  cloud:
    stream:
      bindings:
        highValueConsumer-in-0:
          destination: high-value-transactions # Nome del topic Kafka per le transazioni ad alto valore 
          group: high-value-group # Gruppo consumer per le transazioni ad alto valore 
      kafka:
        binder:
          brokers: localhost:9092 # Broker Kafka 

  kafka:
    bootstrap-servers: localhost:9092 # Indirizzo del broker Kafka 
    streams:
      state-dir: /tmp/kafka-streams # Directory per lo stato di Kafka Streams 
      properties:
        cleanup.on.start: true # Pulisce lo stato all'avvio
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer # Serializzatore per la chiave del produttore 
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer # Serializzatore per il valore del produttore
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer # Deserializzatore per la chiave del consumatore
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer # Deserializzatore per il valore del consumatore
      group-id: banking-group # Gruppo consumer per le transazioni bancarie
      auto-offset-reset: earliest # Imposta il reset automatico dell'offset all'inizio del topic
      properties:
        spring.json.trusted.packages: it.alex.kafka.banking.model # specifica i pacchetti fidati per la deserializzazione
        spring.json.value.default.type: it.alex.kafka.banking.model.TransactionEvent # specifica il tipo di evento da deserializzare

logging:
  level:
    root: ERROR # pulisce i log di Spring Boot
    org.apache.kafka.streams.processor.internals.StateDirectory: OFF # disabilita i log di Kafka Streams
    it.alex.kafka.banking: INFO # log della nostra applicazione

```

**Punti chiave**

Questo file configura l’applicazione Spring Boot per usare Kafka e Kafka Streams in locale. Definisce il nome dell’app `event-driven-banking`, attiva il profilo di sviluppo `dev` e collega tutto al broker Kafka su `localhost:9092`. Imposta un consumer tramite Spring Cloud Stream per leggere dal topic delle transazioni ad alto valore `high-value-transactions`, gestito in un topic separato.

Kafka Streams usa una cartella temporanea per salvare lo stato e viene pulita ad ogni avvio, utile in sviluppo. Producer e consumer Kafka sono configurati per serializzare/deserializzare automaticamente gli eventi in JSON. I log sono mantenuti essenziali: disabilitati quelli di sistema, lasciati solo quelli principali dell’applicazione.

---

## Infrastruttura Docker – `docker-compose.yml`

```yaml
version: '3.8'
services:
  kafka:
    image: bitnami/kafka:latest # Usa l'immagine di Kafka di Bitnami
    container_name: kafka # Nome del container
    ports:
      - "9092:9092"      # porta client
      - "9093:9093"      # porta controller interna (opzionale)
    environment:
      - KAFKA_CFG_PROCESS_ROLES=broker,controller # Abilita il broker e il controller in modalità KRaft
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER # Nome dei listener del controller
      - KAFKA_CFG_NODE_ID=1 # Identificativo univoco del nodo
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 # Votanti nel quorum del controller (node_id@host:controller_port)
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 # Lista di listener: PLAINTEXT per i client, CONTROLLER per il controller interno
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 # Indirizzo pubblicizzato per i client
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT # Mappa di sicurezza tra listener e protocollo
      - ALLOW_PLAINTEXT_LISTENER=yes # Permette connessioni in chiaro
```

**Punti chiave**

Questo docker-compose.yml avvia un container Kafka in modalità KRaft usando l'immagine Bitnami. Kafka ascolta in locale sulla porta 9092 per i client e sulla 9093 per la gestione interna.

Il nodo è identificato come 1 ed è l’unico nel quorum. I listener sono configurati per accettare connessioni non sicure (PLAINTEXT), pubblicizzando il servizio su localhost:9092.

---

## 5 · Descrizione delle classi principali

### 5.1 Bootstrap & utilità

| Classe                            | Package  | Responsabilità                                                                                                                    |
| --------------------------------- | -------- | --------------------------------------------------------------------------------------------------------------------------------- |
| **EventDrivenBankingApplication** | *root*   | Entry‑point `@SpringBootApplication`, avvia il contesto.                                                                          |
| **StartupRunner**                 | *root*   | Implementa `CommandLineRunner`; all’avvio produce 10 `TransactionEvent` e 3 `SecurityAlertEvent` di esempio per popolare i topic. |
| **Topics**                        | `config` | Contiene costanti dei topic Kafka: `transactions`, `security-alerts`, `high-value-transactions`.                                  |

### 5.2 Model (DTO)

| Classe                 | Campi essenziali                                            | Note                                            |
| ---------------------- | ----------------------------------------------------------- | ----------------------------------------------- |
| **TransactionEvent**   | `transactionId`, `accountId`, `amount`, `type`, `timestamp` | Serializzato JSON da Spring Kafka/Cloud Stream. |
| **SecurityAlertEvent** | `alertId`, `accountId`, `severity`, `message`, `timestamp`  | Rappresenta possibili frodi o anomalie.         |

### 5.3 Producer

| Classe                                | Ruolo                                                                                                           | Dettagli |
| ------------------------------------- | --------------------------------------------------------------------------------------------------------------- | -------- |
| **KafkaProducerService**              | Produce `TransactionEvent` sul topic `transactions`. Configurato con `KafkaTemplate<String, TransactionEvent>`. |          |
| **KafkaSecurityAlertProducerService** | Produce `SecurityAlertEvent` sul topic `security-alerts`.                                                       |          |

### 5.4 Consumer (listeners Spring Kafka)

| Classe                                | Topic             | Comportamento                                                                             |
| ------------------------------------- | ----------------- | ----------------------------------------------------------------------------------------- |
| **KafkaConsumerService**              | `transactions`    | Logga ogni transazione (demo). In un ambiente reale chiamerebbe servizi anti‑fraude o DB. |
| **KafkaSecurityAlertConsumerService** | `security-alerts` | Gestisce alert di sicurezza, p.e. invia notifica email/SMS (placeholder).                 |

### 5.5 Kafka Streams

| Classe                   | Descrizione                                                                                 | Punti chiave                                                                                   |
| ------------------------ | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **KafkaStreamsTopology** | Definisce la DSL che filtra le transazioni ≥ 1000 € e inoltra su `high-value-transactions`. | Utilizza `KStream.filter` e `to()`. Garantisce **idempotenza** tramite chiave `transactionId`. |

### 5.6 Consumer Spring Cloud Stream

| Classe                           | Binding                      | Comportamento                                                                                                                       |
| -------------------------------- | ---------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| **HighValueTransactionConsumer** | `highValueTransactions-in-0` | Funzione `Consumer<TransactionEvent>` annotata come `@Bean`. Viene scalata orizzontalmente tramite *consumer group* `hv-consumers`. |

---

## 6 · Stato e persistenza

* **State store RocksDB** creato da Kafka Streams in `kafka-streams-state/`. Contiene una `KTable` per deduplicazione.
* **Offsets** commit automatici: Spring Kafka → poll loop; Kafka Streams → checkpoint es2.

---

## 7 · Test

| Test                                   | Scopo                                                     | Tool                 |
| -------------------------------------- | --------------------------------------------------------- | -------------------- |
| **KafkaStreamsTopologyTest**           | Verifica che solo transazioni ≥ 1000 € vadano all’output. | `TopologyTestDriver` |
| **HighValueTransactionsTopologyTest**  | Variante focalizzata sull’idempotenza.                    |                      |
| **EventDrivenBankingApplicationTests** | Assicura che il contesto Spring si avvii (smoke test).    |                      |

---

## 8 · Guida all’esecuzione

```bash
# 1. Avvia Kafka
$ docker-compose up -d

# 2. Avvia l’app Spring
$ ./mvnw spring-boot:run

# 3. Segui i log (opzionale)
$ docker compose logs -f kafka
```

La classe `StartupRunner` produrrà eventi demo; nei log vedrai il filtro Kafka Streams e il consumer Cloud Stream in azione.

---

## 9 · Estensioni possibili

* **OpenTelemetry** tracing distribuito.
* **Integrazione con database** per audit delle transazioni.
* **Dashboard Grafana** con grafici in tempo reale dei topic.

---

## 10 · Licenza

MIT License. Consultare il file `LICENSE` per i termini completi.
