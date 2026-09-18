# 💧 Hydration Tracker — Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![Docker](https://img.shields.io/badge/Docker-Deployment-blue)
![Tests](https://img.shields.io/badge/Tests-51_passing-success)
![License](https://img.shields.io/badge/License-MIT-green)

A production-deployed REST API for a full-stack **Hydration Tracker** application built with **Java 21 and Spring Boot 4**.

The backend provides JWT-based authentication, user management, water intake tracking, hydration analytics, scheduled notifications, and REST APIs documented with Swagger/OpenAPI.

The application is containerized with Docker and deployed on **Render**, with **Neon PostgreSQL** as the production database.

---

## 🚀 Live Application

**[Hydration Tracker](https://hydrationer.vercel.app/)**

The React frontend communicates with this Spring Boot backend through REST APIs.

---

## ✨ Features

### 🔐 Authentication & Security

- User registration and login
- JWT-based authentication
- BCrypt password hashing
- Stateless Spring Security configuration
- Protected REST endpoints
- Endpoint authorization
- Environment-based secret configuration
- Production CORS configuration

### 👤 Profile Management

- View profile
- Update email
- Update daily hydration goal
- Update timezone
- Enable/disable email notifications
- Enable/disable Telegram notifications
- Change password
- Test email notifications
- Test Telegram notifications

### 💧 Water Intake Management

- Add water intake
- Update water intake
- Delete water intake
- View today's entries
- View water intake history
- Daily water summary
- Custom water amounts
- Request validation for water intake values

### 📊 Dashboard

Provides:

- Daily hydration goal
- Water consumed today
- Remaining water
- Progress percentage
- Today's entry count
- Current streak
- Longest streak

### 📈 Statistics

- Overall hydration statistics
- Weekly statistics
- Monthly statistics
- Progress tracking

### 📧 Email Notifications

- Scheduled hydration reminders
- Daily goal achievement notifications
- User-controlled email notification preference
- Test email endpoint
- Spring Scheduler integration
- JavaMailSender
- Brevo SMTP integration

### 📱 Telegram Notifications

- Scheduled hydration reminders
- Goal achievement notifications
- User-controlled Telegram notification preference
- Test Telegram notification endpoint
- Telegram Bot API integration

---

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| **Java 21** | Programming language |
| **Spring Boot 4.0.6** | Backend framework |
| **Spring Web MVC** | REST API development |
| **Spring Security** | Authentication and authorization |
| **JWT** | Stateless authentication |
| **BCrypt** | Password hashing |
| **Spring Data JPA** | Data persistence |
| **Hibernate** | ORM |
| **PostgreSQL** | Relational database |
| **Bean Validation** | Request validation |
| **Spring Mail** | Email integration |
| **JavaMailSender** | Email delivery |
| **Brevo SMTP** | Production email delivery |
| **Telegram Bot API** | Telegram notifications |
| **Spring Scheduler** | Scheduled reminders |
| **Swagger / OpenAPI** | API documentation |
| **Maven** | Build and dependency management |
| **Docker** | Containerization |
| **Render** | Backend deployment |
| **Neon PostgreSQL** | Production database |
| **JUnit / Mockito** | Automated testing |
| **Testcontainers** | PostgreSQL integration testing |

---

## 🏗 Architecture

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

### Backend Architecture

The application follows a layered architecture:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

Cross-cutting concerns such as security, validation, exception handling, scheduling, and external notification services are handled within their respective application layers.

---

## 📂 Project Structure

```text
src/
├── main/
│   ├── java/com/hydration/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── scheduler/
│   │   ├── security/
│   │   └── service/
│   │       ├── interfaces/
│   │       └── implementations/
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/com/hydration/
        ├── controller/
        ├── service/
        └── HydrationReminderApplicationTests.java
```

---

## 🔑 API Modules

| Module | Functionality |
|---|---|
| **Authentication** | Registration and login |
| **Profile** | Profile management and notification settings |
| **Water** | Water intake CRUD and history |
| **Dashboard** | Daily progress and hydration overview |
| **Statistics** | Weekly, monthly, and overall statistics |
| **Notifications** | Email and Telegram notification functionality |

---

## 📚 API Documentation

The API is documented using **Swagger/OpenAPI**.

### Local

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger UI can be used to explore and test the available REST endpoints.

---

## 🧪 Testing

The backend contains **51 automated tests** covering application startup, service-layer business logic, request validation, and controller behavior.

### Test Suite

```text
Service / Application Tests
├── UserServiceImplTest
├── WaterServiceImplTest
├── ProfileServiceImplTest
└── HydrationReminderApplicationTests

Controller Tests
├── AuthControllerTest
├── WaterControllerTest
├── ProfileControllerTest
├── DashboardControllerTest
└── StatisticsControllerTest
```

### Testing Technologies

- JUnit
- Mockito
- Spring Boot Test
- Spring MVC Test
- Testcontainers
- PostgreSQL

Integration testing uses a temporary PostgreSQL container rather than relying on the developer's local database.

### Run Tests

```bash
mvn test
```

Current test result:

```text
Tests run: 51
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---

## 🔒 Validation & Exception Handling

### Bean Validation

Incoming API requests are validated using Jakarta Bean Validation.

Examples include:

- Required fields
- Email format validation
- Username constraints
- Password constraints
- Water intake amount limits
- Profile data validation

### Global Exception Handling

Centralized exception handling provides consistent API responses for:

- Resource not found
- Validation errors
- Authentication errors
- Duplicate resources
- Business logic exceptions

---

## 🔑 Authentication Flow

```text
Register
   ↓
Login
   ↓
JWT Token Generated
   ↓
Frontend Sends Bearer Token
   ↓
JWT Authentication Filter
   ↓
Spring Security
   ↓
Protected REST Endpoint
   ↓
Controller → Service → Repository
```

---

## 📧 Email Notification Flow

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

## 📱 Telegram Notification Flow

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

## ⏰ Scheduled Notifications

The application uses **Spring Scheduler** to periodically identify users who are eligible for hydration reminders.

The reminder interval is configurable through:

```properties
REMINDER_INTERVAL=
```

Users can independently control email and Telegram notifications through their profile settings.

---

## ⚙️ Environment Variables

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

**Never commit `.env` or production credentials to Git.**

---

## 🚀 Running Locally

### Prerequisites

- Java 21+
- Maven
- PostgreSQL

### Clone Repository

```bash
git clone https://github.com/sunny-kumar-rana/hydration-reminder-backend.git
```

### Navigate to the Project

```bash
cd hydration-reminder-backend
```

### Configure Environment Variables

Create a local `.env` file or configure the required environment variables in your development environment.

### Build the Application

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The backend starts on:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Docker

The backend includes a `Dockerfile` for containerized deployment.

### Build the Image

```bash
docker build -t hydration-reminder-backend .
```

### Run the Container

```bash
docker run -p 8080:8080 \
  -e DB_URL="..." \
  -e DB_USERNAME="..." \
  -e DB_PASSWORD="..." \
  -e JWT_SECRET="..." \
  hydration-reminder-backend
```

Additional environment variables are required when enabling email and Telegram integrations.

---

## 🌐 Deployment

### Frontend

The React frontend is deployed on **Vercel**.

Live application:

**[Hydration Tracker](https://hydrationer.vercel.app/)**

### Backend

The Spring Boot application is containerized using **Docker** and deployed on **Render**.

### Database

Production data is stored in **Neon PostgreSQL**.

### Email

Production email delivery uses **Brevo SMTP**.

### Telegram

Telegram notifications use the **Telegram Bot API**.

---

## 📸 Screenshots

Recommended repository screenshots:

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

## 🔮 Future Improvements

Potential future improvements include:

- Password reset / forgot password
- Custom reminder times
- Weekly email reports
- More detailed hydration analytics
- PDF/Excel report export
- Push notifications
- Mobile application
- Progressive Web App support
- CI/CD pipeline
- Application monitoring
- Improved accessibility

---

## 👨‍💻 Author

Developed as a full-stack portfolio project demonstrating:

- REST API development
- Spring Boot backend architecture
- JWT authentication and authorization
- PostgreSQL persistence
- Request validation
- Scheduled background processing
- Email and Telegram integrations
- Automated unit and integration testing
- Docker containerization
- Cloud deployment

**GitHub:**  
https://github.com/sunny-kumar-rana