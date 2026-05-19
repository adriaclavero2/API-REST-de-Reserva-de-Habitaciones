# 🏨 API REST de Reserva de Habitaciones

> A robust, rule-driven room reservation API built with Spring Boot 3 and an in-memory H2 database — zero external setup required.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![H2](https://img.shields.io/badge/Database-H2%20In--Memory-blue.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## 📖 Overview

This API provides an automated solution for the operational needs of a small accommodation. All business rule enforcement — date overlap control, data consistency, and reservation state management — is handled entirely by the backend logic layer.

To simplify deployment and guarantee a zero-residue development environment, the application uses an **in-memory H2 relational database**. Data is automatically initialized and destroyed with the application lifecycle, streamlining testing and technical demonstration workflows.

---

## ✨ Features

### 👤 User Management (US1 – US2)

- **Guest Registration**: User creation with mandatory name validation and correct email format enforcement.
- **Data Integrity**: Strict duplicate email detection to prevent conflicts in the system.
- **Controlled Queries**: List all users or retrieve a specific one by ID, with proper handling of non-existent resources.

### 🛏️ Room Catalogue (US3 – US4)

- **Inventory Registration**: Rooms identified by unique number, capacity, and price per night.
- **Parameter Validation**: Numeric constraints preventing rooms with zero or negative prices or capacities from being registered.
- **Uniqueness Guarantee**: Blocks registration attempts for room numbers already existing in the database.

### 📅 Reservation System (US5 – US6 – US7)

- **Double Overlap Rule**: An algorithm prevents a room from being booked on dates that overlap with an active reservation. Similarly, a single user cannot hold more than one active reservation in overlapping periods.
- **Flexible Checkout Rule**: The checkout date is not considered occupied — a guest can check in on the same day another checks out, with no false conflicts generated.
- **Cancellation Lifecycle**: Controlled state transitions (ACTIVE to CANCELLED). Double-cancellation attempts are blocked, and dates are immediately released for new bookings upon cancellation.

---

## 🏗 Architecture & Design Decisions

### 1. Strict Layered Architecture (Separation of Concerns)

The application is divided into independent packages with clearly defined responsibilities:

| Layer | Responsibility |
|---|---|
| **Controllers** | REST entry points — validate incoming JSON format and immediately delegate logic |
| **Services** | Orchestrate transactions and enforce business rules |
| **Validators** | Specialized classes for temporal constraint resolution and availability checks |
| **Repositories** | Persistence layer interacting with the database via Spring Data JPA |

### 2. Data Isolation via DTOs and Mappers

JPA entities are never exposed directly through the API endpoints:

- Incoming requests are received via **RequestDTO** classes with Bean Validation (`@NotBlank`, `@Min`, `@Email`).
- Responses are returned encapsulated in **ResponseDTO** objects, hiding sensitive database internals.
- Entity-to-DTO conversion is centralized in dedicated **Mapper** classes, eliminating duplicated logic across controllers and services.

### 3. Global Exception Handling (`@RestControllerAdvice`)

A global interceptor eliminates raw server error traces entirely. Every foreseeable exception generates a uniform JSON response with the appropriate semantic HTTP status code (`400`, `404`, or `409`):

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Room is already reserved for the selected dates",
  "timestamp": "2026-05-19T23:15:45"
}
```

---

## 🛠 Tech Stack

| Category | Technologies |
|---|---|
| **Backend** | Java 21, Spring Boot 3.5.x, Spring Data JPA, Hibernate |
| **Database** | H2 In-Memory (`jdbc:h2:mem:hostalmontsec`) |
| **Validation** | Jakarta Validation API (Hibernate Validator) |
| **Testing** | JUnit 5, Mockito |
| **Tools** | Maven, Git, IntelliJ IDEA, Postman |

---

## 📡 API Endpoints

### Users (`/users`)

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/users` | Register a new user | `201 Created` |
| `GET` | `/users` | List all registered users | `200 OK` |
| `GET` | `/users/{id}` | Get a user by ID | `200 OK` |

### Rooms (`/rooms`)

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/rooms` | Register a room in the catalogue | `201 Created` |
| `GET` | `/rooms` | List all rooms | `200 OK` |
| `GET` | `/rooms/{id}` | Get a room by ID | `200 OK` |

### Reservations (`/reservations`)

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/reservations` | Create a reservation with date validation | `201 Created` |
| `GET` | `/reservations` | List all reservations in the system | `200 OK` |
| `GET` | `/reservations/{id}` | Get a reservation by ID | `200 OK` |
| `GET` | `/users/{userId}/reservations` | Get all reservations for a specific user | `200 OK` |
| `PATCH` | `/reservations/{id}/cancel` | Cancel an active reservation (releases availability) | `204 No Content` |

---

## 📂 Project Structure

```
com.itacademy.api_rest_room_reservation/
├── ApiRestRoomReservationApplication.java   # Application entry point
├── controllers/                              # REST layer (User, Room, Reservation)
├── entities/                                 # JPA entities (User, Room, Reservation)
├── enums/                                    # Reservation states (ReservationStatus)
├── exceptions/                               # Custom exceptions and ExceptionHandler
├── mappers/                                  # Entity <-> DTO transformation
├── repositories/                             # Data access interfaces (Spring Data JPA)
├── requestDTOS/                              # Input validation models
├── responseDTOS/                             # Output format models
└── validators/                               # Pure business validation logic
```

---

## 🚀 Installation & Setup

Since the application uses an in-memory database, **no external database configuration is required**.

### Prerequisites

- Java JDK 21
- Maven 3.6+ *(or use the included Maven Wrapper)*

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/tu-usuario/api-rest-room-reservation.git

# 2. Navigate to the project root
cd api-rest-room-reservation

# 3. Start the application
./mvnw spring-boot:run
```

The server will be available at **http://localhost:8080**

---

## 🧪 Testing

Unit tests verify critical business logic in complete isolation using **JUnit 5** and **Mockito**.

```bash
./mvnw test
```

### Test Coverage

| Test Class | Scenarios Covered |
|---|---|
| `UserServiceTest` | Duplicate email detection and isolation |
| `RoomServiceTest` | Duplicate room number prevention |
| `ReservationServiceTest` | Valid reservation creation · Invalid date rejection (check-in after check-out) · Room overlap exception · User overlap exception · Successful cancellation state transition · Double-cancellation conflict prevention |

---
