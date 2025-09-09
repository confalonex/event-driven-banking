# Event-Driven Banking (Spring Boot + Kafka)

Progetto dimostrativo di un sistema bancario **event-driven** basato su **Spring Boot** e **Apache Kafka**.  
Ogni step della transazione è gestito tramite eventi pubblicati e consumati da topic Kafka.

---

## 🚀 Architettura

**Flusso principale:**
1. Produzione di un `ValidatedTransactionEvent`.
2. Consumo da `NotificationTransactionConsumer` → generazione notifica.
3. `NotificationService` → invio notifica e conferma immediata (`ConfirmedTransactionEvent`).
4. Consumo da `ConfirmedTransactionConsumer` → aggiornamento saldi su `AccountService`.

```text
[Producer] -> ValidatedTransactionEvent
      ↓
[Consumer] NotificationTransactionConsumer
      ↓
[Service] NotificationService (registra + conferma subito)
      ↓
[Producer] ConfirmedTransactionEvent
      ↓
[Consumer] ConfirmedTransactionConsumer
      ↓
[Service] AccountService (applyTransfer)
````

---

## 📂 Struttura pacchetti

* `config` → configurazione Kafka.
* `model` → classi evento (POJO).
* `service` → logica di dominio (notifiche, account, storage eventi).
* `kafka.producer` / `kafka.consumer` → publisher e subscriber Kafka.
* `streams` → elaborazioni Kafka Streams.
* `runner` → `StartupRunner` genera eventi all’avvio.

---

## ⚙️ Configurazione

`application.yml` (estratto):

```yaml
app:
  topic:
    validated-transactions: validated-transactions
    notification-transactions: notification-transactions
    confirmed-transactions: confirmed-transactions

  confirm:
    enabled: true   # scheduler abilitato (conferma avviene subito)
```

---

## ▶️ Esecuzione

1. Avvia Kafka (es. Docker Compose).
2. Run applicazione Spring Boot.
3. Log di esempio:

```text
ValidatedTransactionEvent -> inviato
NotificationService -> notifica inviata per txId=123
NotificationService -> conferma immediata txId=123
ConfirmedTransactionConsumer -> applicato trasferimento txId=123
```

---

## ✨ Caratteristiche

* **Event-Driven**: ogni step è un evento Kafka.
* **Storico eventi in memoria** per debugging.
* **AccountService** mantiene i saldi aggiornati.
* **Idempotenza base** (notifiche già confermate non vengono duplicate).
* Scheduler di conferma **disattivato** ma mantenuto per estensioni future.

---

## 🔮 Estensioni possibili

* REST API / UI per simulare la visualizzazione delle notifiche.
* Persistenza eventi e saldi in database.
* Idempotenza avanzata lato consumer.
