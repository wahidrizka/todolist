# Todolist (Spring Boot 3.5)

API TodoList sederhana dengan **Spring Boot 3.5**, **Maven**, **JUnit 5 / WebMvcTest**, dan **OpenAPI UI (Swagger)**.  
Struktur menekankan alur bersih **DTO → Service → Controller**, siap dikembangkan ke **Docker** & **CI/CD (GitHub
Actions)**.

## Fitur

- `GET /api/health` — health check sederhana
- `POST /api/todos` — buat todo baru
- `GET /api/todos` — daftar semua todo
- Validasi request (Jakarta Validation)
- OpenAPI UI: **/scalar** (JSON: **/v3/api-docs**)

## Tech Stack

- Java 17, Spring Boot 3.5.x
- Maven
- springdoc-openapi 2.8.13
- JUnit 5, Mockito, WebMvcTest

## Menjalankan Lokal

```bash
mvn clean package
mvn spring-boot:run
# Open: http://localhost:8080/scalar
```

## Contoh Request

`POST /api/todos`

```http
POST /api/todos
Content-Type: application/json

{ "title": "Belajar Spring" }
```

Respons:

```json
{
  "id": 1,
  "title": "Belajar Spring",
  "completed": false,
  "createdAt": "2025-01-01T00:00:00Z",
  "updatedAt": "2025-01-01T00:00:00Z"
}
```

## Testing

```bash
# Jalankan semua unit test
mvn test

# Contoh test slice WebMvc
# - HealthControllerTest
# - TodoControllerTest
```

## Struktur Kode (ringkas)

```
src/main/java/com/example/todolist
├─ TodolistApplication.java
├─ api/HealthController.java
└─ todo/
   ├─ CreateTodoRequest.java
   ├─ TodoResponse.java
   ├─ TodoService.java
   ├─ InMemoryTodoService.java
   └─ TodoController.java
```

## Konvensi Commit

Mengikuti **Conventional Commits**:

- `feat:`, `fix:`, `chore:`, `test:`, `docs:`, dst.

## Roadmap Singkat

- [ ] Tambah `PUT /api/todos/{id}` & `DELETE /api/todos/{id}`
- [ ] Persistensi **PostgreSQL** + **Flyway**
- [ ] Kontainerisasi **Docker** & **Docker Compose**
- [ ] **GitHub Actions** (build, test, publish image ke GHCR)
- [ ] Deploy ke **GCE (Compute Engine)** via runner/SSH
- [ ] Observability (healthcheck, log rotation)
