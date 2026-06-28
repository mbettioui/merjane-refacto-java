### Consignes:
* Ignorez les migrations BDD
* Ne pas modifier les classes qui ont un commentaire: `// WARN: Should not be changed during the exercise`

---

## Prerequisites

- **Java 17** via SDKMAN: `sdk env` (reads `.sdkmanrc`)
- **Task**: `brew install go-task/tap/go-task`

## Commands (run from repo root)

| Command | Description |
|---------|-------------|
| `task verify` | Full build: tests + JaCoCo 80% + PMD (mirrors CI) |
| `task test` | Unit tests only |
| `task it` | Integration tests only |
| `task run` | Start application locally |
| `task coverage` | Open JaCoCo HTML report (requires prior `task verify`) |

## API

`POST /orders/{orderId}/processOrder` — processes an order, decrements stock and sends notifications.

Swagger UI (when running locally): http://localhost:8080/api/swagger-ui.html

## Architecture (Hexagonal)

```
api/          Driving ports (inbound interfaces, e.g. ProcessOrderUseCase)
domain/       Pure business logic — no Spring, no JPA
  model/      ProductType enum, Product, Order (POJOs)
  port/       Driven ports (outbound SPI: ProductPort, OrderPort, NotificationPort)
  handler/    Strategy + Template Method handlers per ProductType
infra/
  persistence/ JPA entities, Spring Data repositories, adapters, mapper
  notification/ NotificationService (frozen) + NotificationAdapter
  ui/web/      Spring MVC controller, advice, DTO
  config/      BeanConfig — wires domain POJOs into Spring
```

**Dependency direction**: `infra → domain/api`, never the reverse (enforced by ArchUnit).

**Frozen files** (body unchanged, only package relocated to infra):
- `NotificationService`, `OrderEntity`, `ProcessOrderResponse`
