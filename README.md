# Smart Expense Tracker API

A REST API for tracking personal expenses, built with Java 17 and Spring Boot 3.
Data is stored in memory (a `ConcurrentHashMap`) — no database required, as
per the assignment spec.
 
## What it supports

- `POST   /api/expenses` — add an expense (title, amount, category, date)
- `GET    /api/expenses` — view all expenses (newest first)
- `GET    /api/expenses?category=Food` — filter by category (case-insensitive)
- `DELETE /api/expenses/{id}` — delete an expense
- `GET    /api/expenses/total` — overall total + a breakdown by category
- `GET    /api/expenses/total/{category}` — total for one category

**Bonus implemented:** OpenAPI/Swagger docs, available at
`http://localhost:8080/swagger-ui.html` once the server is running.

## Requirements

- Java 17+
- Maven 3.8+ (or use the included wrapper if you add one — this project
  assumes a system-installed `mvn`)

## Install dependencies

```bash
mvn clean install -DskipTests
```

## Run the server

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`. Swagger UI at
`http://localhost:8080/swagger-ui.html`.

## Run the tests

```bash
mvn test
```

This runs both the service-layer unit tests
(`tests/java/com/expensetracker/service/ExpenseServiceTest.java`) and the
controller integration tests
(`tests/java/com/expensetracker/controller/ExpenseControllerTest.java`,
using `MockMvc` against a real Spring context — no server needs to be running
separately for this).

> Note: `pom.xml` points Maven's test source directory at the top-level
> `tests/` folder (instead of the usual `src/test/java`) to match the
> submission structure required by this assignment.

## Example requests

Add an expense:
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"title":"Groceries","amount":45.99,"category":"Food","date":"2026-07-30"}'
```

Filter by category:
```bash
curl "http://localhost:8080/api/expenses?category=Food"
```

Get totals:
```bash
curl http://localhost:8080/api/expenses/total
```

Delete an expense:
```bash
curl -X DELETE http://localhost:8080/api/expenses/1
```

## Opening in Eclipse

1. Make sure Eclipse has the **m2e** (Maven Integration) plugin — it's
   included by default in the Eclipse IDE for Enterprise Java and Web
   Developers package. If you have a different package: `Help` →
   `Eclipse Marketplace` → search "m2e" → install.
2. `File` → `Import…` → `Maven` → `Existing Maven Projects`.
3. Browse to this project's root folder (the one containing `pom.xml`) and
   click `Finish`.
4. Eclipse will download dependencies and generate `.classpath`/`.project`
   automatically (these are gitignored, so this step is expected).
5. Run the app: right-click
   `src/main/java/com/expensetracker/ExpenseTrackerApplication.java` →
   `Run As` → `Java Application`.
6. Run the tests: right-click the `tests` folder (or an individual test
   class) → `Run As` → `JUnit Test`.

## Project structure 
