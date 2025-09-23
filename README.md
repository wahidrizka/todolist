# todolist

A simple Todo List REST API built with **Java 17** and **Spring Boot 3.5.5**.

This README reflects the current source code in the `dev` branch with:

- Validation using **Jakarta Validation**.
- Error responses standardized with **RFC 7807 Problem Details** (`application/problem+json`).
- Versioned endpoint **`/api/v1/todos`** with **default pagination** (`page=0`, `size=20`).
- Legacy endpoints retained for compatibility.
- Non-production **CORS allow-all** via `@Profile("!prod")`.
- Persistence:
    - **In-memory** service for non-prod profile.
    - **PostgreSQL** via `JdbcTemplate` for `prod` profile (with Flyway migration).

## Requirements

- Java 17
- Maven

## Build & Test

```bash
mvn -B -ntp verify
```

## Run (dev / default profile)

```bash
mvn spring-boot:run
```

API docs available at:

- Scalar UI: `http://localhost:8080/scalar`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Profiles

- **Default / non-prod**: In-memory todo service, permissive CORS (`CorsConfig` with `@Profile("!prod")`).
- **prod**: `JdbcTodoService` with PostgreSQL. Configure datasource and Flyway via `application-prod.properties`.

---

## API Reference (Final)

### Health & Version

- `GET /api/health` → **200 OK**
- `GET /api/version` → **200 OK** (Build & Git info)

### Todos (Legacy)

- `POST /api/todos`  
  Body: `{ "title": "string (required, max 200)" }`  
  Res: **201 Created** → `TodoResponse`
- `GET /api/todos`  
  Query (optional):
    - `q`: string (filter by title, contains)
    - `completed`: boolean
    - `page`: integer ≥ 0
    - `size`: 1..200  
      Res: **200 OK** → `PageResult<TodoResponse>`
- `GET /api/todos/{id}` → **200 OK** → `TodoResponse` | **404 Not Found**
- `PATCH /api/todos/{id}` (partial)  
  Body (optional):
    - `title`: string (max 200)
    - `completed`: boolean  
      Res: **200 OK** → `TodoResponse`
- `DELETE /api/todos/{id}` → **204 No Content** | **404 Not Found**

### Todos v1 (Default Pagination)

- `GET /api/v1/todos`  
  Default: `page=0`, `size=20` (overridable via query params)  
  Query same as legacy.  
  Res: **200 OK** → `PageResult<TodoResponse>`

### Error Format — RFC 7807 (Problem Details)

Content-Type: **`application/problem+json`**

**Body validation (400):**

```json
{
    "type": "about:blank",
    "title": "Request validation failed",
    "status": 400,
    "detail": "One or more fields are invalid.",
    "instance": "/api/todos/1",
    "errors": {
        "title": [
            "title maksimal 200 karakter"
        ]
    }
}
```

**Query validation (400):**

```json
{
    "type": "about:blank",
    "title": "Request parameter validation failed",
    "status": 400,
    "detail": "One or more request parameters are invalid.",
    "instance": "/api/v1/todos",
    "violations": [
        {
            "param": "size",
            "message": "must be greater than 0"
        }
    ]
}
```

**Type mismatch (400, e.g., `size=abc`):**

```json
{
    "type": "about:blank",
    "title": "Parameter type mismatch",
    "status": 400,
    "detail": "One or more request parameters have an invalid type.",
    "instance": "/api/v1/todos",
    "param": "size",
    "expectedType": "Integer",
    "value": "abc"
}
```

### CORS

- **Non-prod (`!prod`)**: allow-all origins/methods/headers (see `CorsConfig`).
- **Prod**: CORS off by default; later switch to an allow-list per requirements.

### Pagination Contract

- **Legacy** `/api/todos`: behavior follows provided parameters (no forced defaults).
- **v1** `/api/v1/todos`: default `page=0`, `size=20`; maximum `size=200`.

---

## Notes

- OpenAPI annotations in controllers document 200 and 400 responses (Problem Details) for `/api/v1/todos`.
- Validation annotations protect `page/size` and request bodies; handlers translate violations into Problem Details.
