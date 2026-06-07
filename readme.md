# Hydration Reminder System

A Spring Boot application that helps users stay hydrated by sending periodic reminders. Users can register with a username and email address, and future versions will support Telegram notifications, scheduled reminders, water intake tracking, and a React frontend.

## Current Status

### Completed Features

* User Registration API
* Request Validation
* Custom Exception Handling
* Global Exception Handling
* Oracle Database Integration
* JPA/Hibernate Persistence
* Automatic Entity Timestamping

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* Oracle Database
* Lombok
* Jakarta Validation

### Planned Frontend

* React.js

---

## Project Structure

```text
src/main/java/com/hydration

├── controller
│   └── UserController
│
├── dto
│   ├── RegisterRequest
│   ├── RegisterResponse
│   └── ErrorResponse
│
├── entity
│   └── User
│
├── exception
│   ├── UsernameAlreadyExistsException
│   ├── EmailAlreadyExistsException
│   └── GlobalExceptionHandler
│
├── repository
│   └── UserRepository
│
└── service
    └── UserService
```

---

## Database Design

### Users Table

| Column           | Description                 |
| ---------------- | --------------------------- |
| id               | Primary Key                 |
| username         | Unique Username             |
| email            | Unique Email                |
| password         | User Password               |
| telegram_chat_id | Telegram Chat ID (nullable) |
| created_at       | Account Creation Timestamp  |

---

## API Endpoints

### Register User

```http
POST /api/auth/register
```

Request:

```json
{
  "username": "shubh123",
  "email": "shubh123@gmail.com",
  "password": "secret123"
}
```

Success Response:

```json
{
  "username": "shubh123",
  "message": "User shubh123 Registered Successfully"
}
```

---

## Validation Rules

### Username

* Required
* Minimum 5 characters
* Maximum 20 characters

### Email

* Required
* Must be a valid email format

### Password

* Required
* Minimum 6 characters
* Maximum 16 characters

---

## Error Handling

### Duplicate Username

```json
{
  "message": "Username already exists",
  "status": 409
}
```

### Duplicate Email

```json
{
  "message": "Email already exists",
  "status": 409
}
```

### Validation Failure

```json
{
  "username": "Username must be between 5 and 20 characters",
  "email": "Email must be valid"
}
```

---

## Learning Objectives

This project is being built as a learning-focused full-stack application to understand:

* Layered Architecture
* REST API Development
* DTO Pattern
* Spring Data JPA
* Bean Validation
* Global Exception Handling
* Dependency Injection
* Oracle Database Integration
* React and Spring Boot Integration

---

## Upcoming Features

### Phase 2

* Password Encryption (BCrypt)
* User Login API
* Authentication Flow

### Phase 3

* Water Intake Tracking
* Daily Water Logs
* Water Consumption Statistics

### Phase 4

* Reminder Scheduling
* Spring Scheduler
* Configurable Reminder Intervals

### Phase 5

* Telegram Bot Integration
* Telegram Account Linking
* Telegram Notifications

### Phase 6

* React Frontend
* Registration Page
* Login Page
* Dashboard
* Water Tracking UI

### Phase 7

* Deployment
* Backend Hosting
* Frontend Hosting
* Production Configuration

---

## Author

Built as a Spring Boot + React learning project with a focus on industry-standard architecture and best practices.
