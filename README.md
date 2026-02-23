# 📚 Book Library — Spring Boot REST API Demo

A complete CRUD REST API built with Spring Boot, Spring Data JPA, and H2 (in-memory DB).

## Quick Start

```bash
# Terminal 1 — Spring Boot backend
mvn spring-boot:run
# → http://localhost:8080/api/books

# Terminal 2 — Vue 3 + Vite frontend
cd vue-frontend
npm install
npm run dev
# → http://localhost:5173
```

Vite proxies `/api/*` requests to the Spring Boot backend — no CORS configuration needed.

---

## Project Structure

```
src/main/java/com/demo/library/
├── LibraryApplication.java          ← @SpringBootApplication entry point
├── model/
│   └── Book.java                    ← @Entity (maps to DB table)
├── repository/
│   └── BookRepository.java          ← extends JpaRepository (free CRUD!)
├── controller/
│   └── BookController.java          ← @RestController with all endpoints
└── exception/
    └── GlobalExceptionHandler.java  ← @RestControllerAdvice for error handling
```

---

## API Endpoints

| Method   | URL                         | Description           |
|----------|-----------------------------|-----------------------|
| `GET`    | `/api/books`                | Get all books         |
| `GET`    | `/api/books?author=Tolkien` | Filter by author      |
| `GET`    | `/api/books?keyword=clean`  | Search by title       |
| `GET`    | `/api/books/{id}`           | Get book by ID        |
| `POST`   | `/api/books`                | Create a new book     |
| `PUT`    | `/api/books/{id}`           | Update a book         |
| `DELETE` | `/api/books/{id}`           | Delete a book         |

---

## curl Examples

```bash
# Get all books (5 seeded at startup)
curl http://localhost:8080/api/books

# Get a single book
curl http://localhost:8080/api/books/1

# Search by title keyword
curl "http://localhost:8080/api/books?keyword=clean"

# Create a new book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot in Action",
    "author": "Craig Walls",
    "publishedYear": 2016,
    "description": "A practical guide to Spring Boot."
  }'

# Update a book
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Hobbit (Updated)",
    "author": "J.R.R. Tolkien",
    "publishedYear": 1937,
    "description": "Updated description."
  }'

# Delete a book
curl -X DELETE http://localhost:8080/api/books/1

# Trigger a validation error (blank title → 400)
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "", "author": "Someone", "publishedYear": 2020}'
```

---

## Useful URLs

| URL | Description |
|-----|-------------|
| `http://localhost:8080/api/books` | Main API |
| `http://localhost:8080/h2-console` | H2 DB browser (JDBC URL: `jdbc:h2:mem:librarydb`) |
| `http://localhost:8080/actuator/health` | Health check |
| `http://localhost:8080/actuator/metrics` | App metrics |

---

## Run Tests

```bash
mvn test
```

Tests use MockMvc — no external server needed, runs in milliseconds.

---

## Key Spring Boot Concepts Demonstrated

- **Auto-configuration** — zero XML, zero boilerplate server setup  
- **@SpringBootApplication** — single annotation to bootstrap everything  
- **Spring Data JPA** — repository with free CRUD + custom query methods  
- **@RestController** — automatic JSON serialization  
- **@Valid** — declarative request body validation  
- **ResponseEntity** — fine-grained HTTP response control (status, headers, body)  
- **@RestControllerAdvice** — centralized error handling  
- **H2** — in-memory DB, zero setup, includes web console  
- **Actuator** — production-ready health/metrics endpoints  
