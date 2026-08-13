# 💧 Hydration Tracker - Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![Docker](https://img.shields.io/badge/Docker-Deployment-blue)
![License](https://img.shields.io/badge/License-MIT-green)

A RESTful backend for the **Hydration Tracker** application built using **Java 21 and Spring Boot**.

The backend provides secure JWT-based authentication, water intake tracking, hydration statistics, profile management, scheduled notifications, and REST APIs documented with Swagger/OpenAPI.

The application is deployed in production using **Docker on Render**, with **Neon PostgreSQL** as the production database.

---

# 🚀 Live Application

**[Hydration Tracker](https://hydrationer.vercel.app/)**

The React frontend communicates with this Spring Boot backend through REST APIs.

---

# ✨ Features

## 🔐 Authentication

* User Registration
* User Login
* JWT Authentication
* BCrypt Password Hashing
* Stateless Security
* Protected REST APIs
* Endpoint Authorization

---

## 👤 User Profile

* View Profile
* Update Email
* Update Daily Water Goal
* Update Timezone
* Enable/Disable Email Notifications
* Enable/Disable Telegram Notifications
* Change Password
* Test Email Notifications
* Test Telegram Notifications

---

## 💧 Water Intake Management

* Add Water Intake
* Update Water Intake
* Delete Water Intake
* Today's Water Entries
* Water History
* Daily Water Summary
* Custom Water Amounts

---

## 📊 Dashboard

Provides:

* Daily Goal
* Water Consumed Today
* Remaining Water
* Daily Progress Percentage
* Current Streak
* Longest Streak
* Today's Entry Count

---

## 📈 Statistics

* Overall Statistics
* Weekly Statistics
* Monthly Statistics

---

## 📧 Email Notification System

* Scheduled Hydration Reminder Emails
* Daily Goal Achievement Emails
* User-controlled Email Notification Preference
* Test Email Notifications
* Spring Scheduler Integration
* JavaMailSender Integration
* Brevo SMTP Integration

Email delivery uses **Brevo SMTP** in the production environment.

---

## 📱 Telegram Notifications

* Scheduled Hydration Reminder Messages
* Goal Achievement Messages
* User-controlled Telegram Notification Preference
* Test Telegram Notifications
* Telegram Bot API Integration

---

# 🛡 Security

* Spring Security
* JWT Authentication
* BCrypt Password Hashing
* Protected API Endpoints
* Stateless Sessions
* Environment-based Secrets
* Production CORS Configuration

Sensitive credentials are stored using environment variables rather than being committed to source control.

---

# 🛠 Tech Stack

| Technology        | Usage                          |
| ----------------- | ------------------------------ |
| Java 21           | Programming Language           |
| Spring Boot 4.0.6 | Backend Framework              |
| Spring Security   | Authentication & Authorization |
| JWT               | Secure Authentication          |
| Spring Data JPA   | Database Access                |
| Hibernate         | ORM                            |
| PostgreSQL        | Database                       |
| Neon PostgreSQL   | Production Database            |
| JavaMailSender    | Email Notifications            |
| Brevo SMTP        | Production Email Delivery      |
| Telegram Bot API  | Telegram Notifications         |
| Spring Scheduler  | Scheduled Notifications        |
| Bean Validation   | Request Validation             |
| Swagger / OpenAPI | API Documentation              |
| Maven             | Dependency Management          |
| Docker            | Production Deployment          |
| Render            | Backend Hosting                |

---

# 🏗 Architecture

```text
                         ┌─────────────────────┐
                         │        User         │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   React Frontend    │
                         │       Vercel        │
                         └──────────┬──────────┘
                                    │
                              REST / JSON
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   Spring Boot API   │
                         │       Render        │
                         └──────┬──────┬───────┘
                                │      │
                    ┌───────────┘      └────────────┐
                    ▼                              ▼
          ┌──────────────────┐             ┌──────────────────┐
          │ Neon PostgreSQL  │             │  Notifications   │
          └──────────────────┘             └───────┬──────────┘
                                                   │
                                      ┌────────────┴────────────┐
                                      ▼                         ▼
                               ┌──────────────┐          ┌──────────────┐
                               │    Brevo     │          │   Telegram   │
                               │    SMTP      │          │   Bot API    │
                               └──────────────┘          └──────────────┘
```

The backend follows a layered architecture:

```text
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

```text
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

* Authentication
* Profile
* Water Intake
* Dashboard
* Statistics
* Notifications

---

# 📚 API Documentation

Swagger/OpenAPI is available for API exploration and testing.

### Local

```text
http://localhost:8080/swagger-ui/index.html
```

The production API is deployed on Render.

---

# ⚙️ Environment Variables

Sensitive configuration is supplied through environment variables.

Example:

```properties
# Database

DB_URL=
DB_USERNAME=
DB_PASSWORD=

# JWT

JWT_SECRET=

# Email - Brevo SMTP

MAIL_HOST=smtp-relay.brevo.com
MAIL_PORT=2525
MAIL_USERNAME=
MAIL_PASSWORD=
MAIL_FROM=

# Telegram

TELEGRAM_BOT_TOKEN=
TELEGRAM_API_URL=https://api.telegram.org

# Scheduler

REMINDER_INTERVAL=

# Server

PORT=
```

The `.env` file should **never be committed to Git**.

---

# 🚀 Running Locally

## Prerequisites

* Java 21+
* Maven
* PostgreSQL
* Node.js for the frontend

---

## Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/hydration-tracker-backend.git
```

---

## Navigate to the Project

```bash
cd hydration-tracker-backend
```

---

## Configure Environment Variables

Create a local `.env` file or configure the required environment variables in your development environment.

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

The backend starts on:

```text
http://localhost:8080
```

---

# 🔒 Authentication Flow

```text
Register
   ↓
Login
   ↓
JWT Token Generated
   ↓
Frontend Stores Token
   ↓
Bearer Token Added to Requests
   ↓
Spring Security Validates Token
   ↓
Protected API Access
```

---

# 📧 Email Notification Flow

```text
Spring Scheduler
       ↓
Find Eligible Users
       ↓
Email Notifications Enabled?
       ↓
Determine Notification
       ↓
JavaMailSender
       ↓
Brevo SMTP
       ↓
Recipient
```

---

# 📱 Telegram Notification Flow

```text
Spring Scheduler
       ↓
Find Eligible Users
       ↓
Telegram Notifications Enabled?
       ↓
Build Notification Message
       ↓
Telegram Bot API
       ↓
User's Telegram Chat
```

---

# ⏰ Scheduled Notifications

The application uses **Spring Scheduler** to periodically check users who are eligible for hydration reminders.

The reminder interval is configurable through:

```properties
REMINDER_INTERVAL=
```

Users can control whether they receive email and Telegram notifications through their profile settings.

---

# ✅ Validation

The backend validates incoming requests using **Bean Validation**.

Examples include:

* Required fields
* Email format validation
* Positive water intake values
* Password validation
* Request data validation

---

# ❗ Global Exception Handling

Centralized exception handling provides consistent API responses for:

* Resource Not Found
* Validation Errors
* Authentication Errors
* Duplicate Resources
* Business Logic Exceptions

---

# 🧪 Testing

The APIs can be tested using:

* Swagger UI
* Postman
* Bruno
* Insomnia

The production application can also be tested directly through the deployed frontend.

---

# 🌐 Deployment

## Frontend

The React frontend is deployed using **Vercel**.

**Live Application:**

https://hydrationer.vercel.app/

## Backend

The Spring Boot backend is containerized using **Docker** and deployed on **Render**.

## Database

The production database is hosted on **Neon PostgreSQL**.

## Email

Production email delivery uses **Brevo SMTP**.

## Telegram

Telegram notifications use the **Telegram Bot API**.

---

# 🔮 Future Improvements

Potential future improvements include:

* Password Reset / Forgot Password
* Custom Reminder Times
* Weekly Email Reports
* More Detailed Hydration Analytics
* PDF/Excel Report Export
* Push Notifications
* Mobile Application
* Progressive Web App Support
* Automated Unit & Integration Testing
* CI/CD Pipeline
* Application Monitoring
* Improved Accessibility

---

# 📸 Screenshots

Recommended screenshots for the repository:

```text
screenshots/
├── login.png
├── register.png
├── dashboard.png
├── profile.png
├── statistics.png
└── swagger.png
```

---

# 👨‍💻 Author

Developed as a full-stack portfolio project demonstrating:

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* PostgreSQL
* REST API Development
* React Integration
* Email Notifications
* Telegram Notifications
* Scheduled Tasks
* Docker
* Cloud Deployment

---

# ⭐ License

This project is licensed under the MIT License.
