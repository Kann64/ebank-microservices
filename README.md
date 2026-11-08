# ebank-microservices

Event-driven banking microservices in Spring Cloud with:

- **Eureka** service discovery (`discovery-server`)
- **Spring Cloud Gateway** (`api-gateway`)
- **OpenFeign** inter-service calls (`chatbot-service` -> `account-service`)
- **Kafka** event publishing/consumption (`transactions.created`)
- **LLM-powered Telegram chatbot** notifications (`chatbot-service`)

## Services

- `discovery-server` (`:8761`) — Eureka registry
- `api-gateway` (`:8080`) — routes `/api/accounts/**` to `account-service`
- `account-service` (`:8081`) — creates transactions and publishes Kafka events
- `chatbot-service` (`:8082`) — consumes transaction events, enriches via Feign, generates LLM text, sends Telegram messages
- `common-events` — shared Kafka event contracts

## Event flow

1. Client calls `POST /api/accounts/{accountId}/transactions` via gateway
2. `account-service` publishes `TransactionCreatedEvent` to Kafka topic `transactions.created`
3. `chatbot-service` consumes the event
4. `chatbot-service` fetches account summary via OpenFeign
5. `chatbot-service` generates a response via configured LLM API
6. Telegram notification is sent to the configured chat

## Run locally

Start Kafka:

```bash
docker compose up -d
```

Build all modules:

```bash
mvn clean verify
```

Run services (separate terminals):

```bash
mvn -pl discovery-server spring-boot:run
mvn -pl account-service spring-boot:run
mvn -pl chatbot-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

### Environment variables

- `EUREKA_URL` (default `http://localhost:8761/eureka`)
- `KAFKA_BOOTSTRAP_SERVERS` (default `localhost:9092`)
- `TELEGRAM_BOT_TOKEN`
- `TELEGRAM_CHAT_ID`
- `LLM_API_URL`
- `LLM_API_KEY`
- `LLM_MODEL` (default `gpt-4o-mini`)

### Example request

```bash
curl -X POST 'http://localhost:8080/api/accounts/acc-100/transactions' \
  -H 'Content-Type: application/json' \
  -d '{"amount":120.50,"currency":"USD","description":"Coffee"}'
```
