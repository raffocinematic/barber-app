# 💈 Barber App

A web application built with **Spring Boot** for managing barber shop appointments.

This project is a **backend-focused learning project** aimed at improving Java backend development skills, including Spring ecosystem tools and database interaction.

---

# 📌 Overview

Barber App allows users to:

- Register a new account
- Log in securely
- Book barber services
- View their appointments

The project focuses on **backend architecture, authentication, and database interaction** rather than frontend complexity.

---

# 🛠 Tech Stack

Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Liquibase
- PostgreSQL
- Maven

Frontend

- Thymeleaf
- HTML / CSS

---

# 🏗 Architecture

The application follows a **layered architecture** typical of Spring Boot applications.

Controller → Service → Repository → Database


### Responsibilities

| Layer | Description |
|------|-------------|
Controller | Handles HTTP requests and responses |
Service | Contains business logic |
Repository | Handles database access using Spring Data JPA |
Model | JPA entities mapped to database tables |
DTO | Form objects used for data transfer |
Config | Security and infrastructure configuration |

---

# 📂 Project Structure

src/main/java/com/example/barber_app

config
├── SecurityConfig
└── PasswordConfig

controller
├── AuthController
└── BookingController

service
├── UserService
├── RegistrationService
└── BookingService

repository
├── UserRepository
└── AppointmentRepository

model
├── User
├── Appointment
└── ServiceType

dto
└── RegisterForm


---

# 🔐 Authentication & Security

Authentication is implemented using **Spring Security**.

### Key Features

- Custom login page
- Password hashing with **BCrypt**
- User authentication through database
- Protected routes
- Session-based authentication

### Authentication Flow

1. User submits login form (`/login`)
2. Spring Security intercepts the request
3. `UserService` loads user from database
4. Password is verified using `BCryptPasswordEncoder`
5. If valid → user is authenticated
6. User is redirected to `/booking`

---

# 👤 User Registration

Users can register through `/register`.

### Registration Flow

1. User submits:

username

password

confirmPassword


2. Backend validation checks:

- Username uniqueness
- Password confirmation

3. Password is encrypted using **BCrypt**

4. User is saved in the database.

---

# 🗄 Database

Database: **PostgreSQL**

Schema management is handled by **Liquibase**.

### Main Tables

#### users

| Column | Description |
|------|-------------|
id | Primary key |
username | Unique username |
password_hash | Encrypted password |
role | User role (ROLE_USER) |

#### appointments

| Column | Description |
|------|-------------|
id | Primary key |
customer_id | Foreign key → users.id |
service_type | Type of service |
start_time | Appointment start |
end_time | Appointment end |
status | Appointment status |

---

# ⚙ Database Migrations

Liquibase is used for **database versioning and migrations**.

Migration files are located in: 
src/main/resources/db


Liquibase ensures that database schema changes are **tracked and reproducible across environments**.

---

# ▶ How to Run the Project

### 1️⃣ Start PostgreSQL

Create the database:

```sql
CREATE DATABASE barber_booking;

2️⃣ Configure database connection

application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/barber_booking
spring.datasource.username=postgres
spring.datasource.password=your_password

2️⃣ Configure database connection

application.properties

4️⃣ Open the application
http://localhost:8080

