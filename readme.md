# 💧 Hydration Reminder

A full-stack hydration reminder application built with **Spring Boot** and **React.js**. The goal of this project is to help users build healthy hydration habits by sending scheduled reminders through multiple notification channels such as Email and Telegram.

This project is being developed incrementally while following industry-standard Spring Boot architecture and best practices.

---

## 🚀 Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
* Hibernate
* Oracle Database
* Jakarta Bean Validation
* Lombok
* Maven

### Frontend (Planned)

* React.js
* React Router
* Axios
* Tailwind CSS / Ant Design (TBD)

---

## 📁 Project Structure

```text
src/main/java/com/hydration

├── config
│   └── SecurityConfig
│
├── controller
│   └── UserController
│
├── dto
│   ├── RegisterRequest
│   ├── RegisterResponse
│   ├── LoginRequest
│   ├── LoginResponse
│   └── ErrorResponse
│
├── entity
│   └── User
│
├── exception
│   ├── UsernameAlreadyExistsException
│   ├── EmailAlreadyExistsException
│   ├── InvalidCredentialsException
│   └── GlobalExceptionHandler
│
├── repository
│   └── UserRepository
│
├── service
│   └── UserService
│
└── HydrationReminderApplication
```

---

## ✅ Features Implemented

### User Registration

* User registration API
* Username uniqueness validation
* Email uniqueness validation
* Request validation using Jakarta Validation
* Global exception handling
* Structured API responses
* Oracle database integration

### Authentication

* Login using username
* Password hashing using BCrypt
* Password verification using `PasswordEncoder.matches()`
* Secure authentication flow
* Generic authentication failure responses to prevent username enumeration

### Error Handling

* Global exception handling using `@RestControllerAdvice`
* Custom exceptions
* Validation error handling
* Proper HTTP status codes

---

## 📌 API Endpoints

### Register

```http
POST /api/auth/register
```

Example Request

```json
{
  "username": "shubh123",
  "email": "shubh123@gmail.com",
  "password": "secret123"
}
```

---

### Login

```http
POST /api/auth/login
```

Example Request

```json
{
  "username": "shubh123",
  "password": "secret123"
}
```

---

## 🗄 Database

Current User table fields:

| Column           | Description                |
| ---------------- | -------------------------- |
| id               | Primary Key                |
| username         | Unique Username            |
| email            | Unique Email               |
| password         | BCrypt Hashed Password     |
| telegram_chat_id | Nullable                   |
| created_at       | Account Creation Timestamp |

---

## 🔒 Security

* BCrypt password hashing
* Passwords are never stored in plain text
* Username enumeration protection
* Generic login failure responses
* Constructor-based dependency injection
* Spring Security `PasswordEncoder`

---

## 📚 Concepts Covered

* Layered Architecture
* REST APIs
* Dependency Injection
* IoC Container
* DTO Pattern
* Repository Pattern
* Constructor Injection
* Bean Validation
* Global Exception Handling
* Spring Data JPA
* Optional
* ResponseEntity
* BCrypt Password Hashing
* Spring Configuration
* Bean Creation
* Custom Exceptions

---

## 🛠 Planned Features

### Authentication

* JWT Authentication
* Refresh Tokens
* Remember Me

### Hydration

* Daily Water Intake Logging
* Custom Daily Water Goal
* Water Consumption History
* Daily Statistics

### Reminder System

* Spring Scheduler
* Custom Reminder Interval
* Reminder History

### Notifications

* Telegram Bot Integration
* Email Notifications
* Multiple Notification Channels

### Frontend

* User Registration
* Login
* Dashboard
* Water Intake Tracking
* Reminder Settings
* User Profile

### Deployment

* Docker
* CI/CD
* Cloud Deployment
* Production Database

---

## 🎯 Learning Goals

This project focuses on building a production-style Spring Boot application while understanding every concept from first principles instead of simply copying code.

Topics include:

* Spring Boot Fundamentals
* Spring Security
* Authentication
* REST API Design
* Database Design
* Backend Architecture
* Full-Stack Development
* Clean Code Practices

---

## 📈 Current Progress

* ✅ Project Setup
* ✅ User Registration
* ✅ Login Authentication
* ✅ Password Encryption
* ⏳ JWT Authentication
* ⏳ Water Tracking
* ⏳ Reminder Scheduling
* ⏳ Telegram Integration
* ⏳ React Frontend
* ⏳ Deployment

---

## 👨‍💻 Author

Built as a learning-focused full-stack project to explore modern Java backend development using Spring Boot and React while following production-oriented architecture and best practices.
