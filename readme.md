# 💧 Hydration Tracker - Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![License](https://img.shields.io/badge/License-MIT-green)

A RESTful backend for the **Hydration Tracker** application built using **Spring Boot**. It provides secure JWT-based authentication, water intake tracking, hydration statistics, profile management, scheduled email reminders, and comprehensive REST APIs documented with Swagger.

---

# ✨ Features

## 🔐 Authentication

- User Registration
- User Login
- JWT Authentication
- BCrypt Password Encryption
- Stateless Security
- Protected REST APIs

---

## 👤 User Profile

- View Profile
- Update Email
- Update Daily Water Goal
- Update Timezone
- Enable/Disable Email Notifications
- Change Password

---

## 💧 Water Intake Management

- Add Water Intake
- Update Water Intake
- Delete Water Intake
- Today's Water Entries
- Water History
- Daily Water Summary

---

## 📊 Dashboard

Provides:

- Daily Goal
- Water Consumed Today
- Remaining Water
- Daily Progress Percentage
- Current Streak
- Longest Streak
- Today's Entry Count

---

## 📈 Statistics

- Overall Statistics
- Weekly Statistics
- Monthly Statistics

---

## 📧 Email Notification System

- Scheduled Hydration Reminder Emails
- Goal Achievement Emails
- User-controlled Email Notification Preference
- Spring Scheduler Integration
- JavaMailSender Integration

---

## 🛡 Security

- Spring Security
- JWT Authentication
- Password Encryption
- Endpoint Authorization
- Stateless Sessions

---

# 🛠 Tech Stack

| Technology | Usage |
|------------|------|
| Java 21 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Secure Authentication |
| Spring Data JPA | Database Access |
| Hibernate | ORM |
| PostgreSQL | Database |
| Java Mail Sender | Email Notifications |
| Spring Scheduler | Scheduled Tasks |
| Bean Validation | Request Validation |
| Swagger / OpenAPI | API Documentation |
| Maven | Dependency Management |

---

# 🏗 Architecture

```
Client
      │
      ▼
Controllers
      │
      ▼
Services
      │
      ▼
Repositories
      │
      ▼
PostgreSQL
```

Project follows a layered architecture:

```
Controller
     ↓
Service
     ↓
Repository
     ↓
Database
```

---

# 📂 Project Structure

```
src
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── repository
├── service
│   ├── interfaces
│   └── implementations
├── scheduler
├── security
├── configuration
├── exceptions
└── mapper
```

---

# 🔑 API Modules

- Authentication
- Profile
- Water
- Dashboard
- Statistics

---

# 📚 Swagger Documentation

Swagger UI is enabled during development.

```
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙ Environment Variables

Configure the following inside `application.properties`.

```properties
# Database

spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JPA

spring.jpa.hibernate.ddl-auto=update

# JWT

jwt.secret=

# Email

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

# 🚀 Running the Project

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/hydration-tracker-backend.git
```

---

## Navigate

```bash
cd hydration-tracker-backend
```

---

## Install Dependencies

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

Application starts on

```
http://localhost:8080
```

---

# 🔒 Authentication Flow

```
Register
      ↓
Login
      ↓
JWT Token Generated
      ↓
Store Token
      ↓
Include Bearer Token
      ↓
Access Protected APIs
```

---

# 📧 Reminder Flow

```
Scheduler
      ↓
Find Eligible Users
      ↓
Email Notifications Enabled?
      ↓
Daily Goal Achieved?
      ↓
Send Reminder Email
```

---

# ✅ Validation

The backend validates incoming requests using Bean Validation.

Examples include:

- Required fields
- Email format
- Positive water intake
- Password validation

---

# ❗ Global Exception Handling

Centralized exception handling for:

- Resource Not Found
- Validation Errors
- Authentication Errors
- Duplicate Resources
- Business Logic Exceptions

---

# 🧪 Testing

You can test the APIs using:

- Swagger UI
- Postman
- Bruno
- Insomnia

---

# 🚀 Future Improvements

- Telegram Notifications
- Push Notifications
- Reminder Time Customization
- Weekly Email Reports
- Export Reports (PDF/Excel)
- Docker Support
- CI/CD Pipeline
- Cloud Deployment
- Monitoring & Logging
- Unit & Integration Tests

---

# 📸 Screenshots

Add screenshots after deployment.

Example:

```
screenshots/

├── swagger.png
├── login.png
├── dashboard.png
├── profile.png
└── statistics.png
```

---

# 👨‍💻 Author

Developed as a portfolio project demonstrating modern backend development using:

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- PostgreSQL
- JavaMailSender
- REST APIs
- Layered Architecture

---

# ⭐ If you found this project useful, consider giving it a star.