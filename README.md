# Smart Booking System

This is a full-stack Smart Booking application built using **Spring Boot (Java)** for the backend and **Angular** for the frontend. It supports booking resources such as cars, rooms, or doctors with user-friendly features including **JWT authentication**, **2FA**, **role-based access**, and **notifications**.

---

## 🎓 Technologies Used

### Backend (Java + Spring Boot)

* Spring Boot
* Spring Security with JWT
* MySQL
* JPA/Hibernate
* Two-Factor Authentication (2FA) via Email
* RESTful API

### Frontend (Angular)

* Angular 17 (with Standalone Components)
* Angular Router
* Reactive Forms
* Angular Material
* HTTP Interceptors

---

## ✨ Key Features

### 🔐 Authentication & Security

* Secure JWT-based login/registration
* Two-Factor Authentication with email verification
* Role-based access: `USER`, `OWNER`, `ADMIN`

### 🏢 Booking Management

* Book any available resource for a selected time
* Overlap check and validation logic
* View your bookings in a list

### 📃 Resource Management

* List all available resources
* Admins/owners can add/manage their own

### 📢 Notification Center

* Shows notifications for actions like booking status
* Automatically fetched for the current user

---

## 📅 How to Run Locally

### 📃 Backend

```bash
cd smart-booking-backend
./mvnw spring-boot:run
```

* Make sure MySQL is running
* Set DB credentials in `application.properties`

### 📄 Frontend

```bash
cd smart-booking-frontend
npm install
ng serve
```

Visit: `http://localhost:4200`

---

## 📣 Roles Overview

| Role  | Permissions                                            |
| ----- | ------------------------------------------------------ |
| USER  | Book resources, view personal bookings & notifications |
| OWNER | Manage resources they own                              |
| ADMIN | Manage all resources & users                           |

---

## 📈 Project Structure

* **smart-booking-backend/**: Java backend with services, repositories, DTOs
* **smart-booking-frontend/**: Angular app with modular, clean UI

---

## 🚀 Why This Project?

This project showcases my ability to:

* Build production-ready full-stack applications
* Implement secure login, 2FA, and access control
* Work with real-world concepts like booking logic and resource availability

---

## ✨ Author

**Marwan Soltan**
[GitHub @Marwan11500](https://github.com/Marwan11500)

---

## 📅 License

This project is licensed under the MIT License.
