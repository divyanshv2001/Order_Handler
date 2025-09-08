# Event-Driven Order Processing System

📝 **Note:** This project was built as part of a **coding assignment for interview evaluation**.  
It demonstrates how a fresher-level Java developer can design and implement an **event-driven backend system** using Spring Boot, MySQL, JPA, and AOP.

---

## 📌 About the Project
This project is a simplified **backend system** that simulates how an e-commerce platform processes order events.  
It was built as part of a coding assignment to demonstrate skills in **Java, Spring Boot, MySQL, JPA, and Aspect-Oriented Programming (AOP)**.

Instead of exposing APIs, the system ingests events from a **text file with JSON objects (one per line)**. Each event updates the order state and notifies observers about changes.

---

## 🎯 Objectives
- Design and implement an **Order** domain model with event history.
- Support multiple event types:
  - **OrderCreatedEvent**
  - **PaymentReceivedEvent**
  - **ShippingScheduledEvent**
  - **OrderCancelledEvent**
- Process events to update the order status (`PENDING`, `PAID`, `PARTIALLY_PAID`, `SHIPPED`, `CANCELLED`).
- Notify observers when an order changes (via Aspect + Observer pattern).
- Persist all orders and events in a **MySQL database**.

---

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA (Hibernate)**
- **Spring AOP**
- **MySQL** (tested on v8.x)
- **Maven**

---

## 📂 Project Structure
src/main/java/com/example/demo/
├─ annotation/ # @NotifyObservers annotation
├─ aspect/ # Aspect to notify observers
├─ domain/ # JPA entities (Order, EventRecord, etc.)
├─ events/ # Event POJOs
├─ ingest/ # File reader to ingest JSON events
├─ observer/ # Observers (LoggerObserver, AlertObserver)
├─ processor/ # EventProcessor (business logic)
└─ repository/ # Spring Data repositories
src/main/resources/
├─ application.properties # DB + app configs
└─ events/sample-events.txt # Input events (JSON lines)

---
## ⚙️ Setup Instructions

### 1. Clone the repo
```bash
git clone https://github.com/divyanshv2001/Order_Handler.git
cd Order_Handler
```
### Create MySQL database:
```bash
CREATE DATABASE orderdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orderdb;
```
### Update DB credentials
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/orderdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password_here
```
### Build & run
```bash
mvn clean package
mvn spring-boot:run
```
### Validation
```bash
SELECT * FROM orders;
SELECT * FROM event_records;
SELECT * FROM order_items;
```
