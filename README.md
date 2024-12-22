# Microservices Architecture: Order-Service, Inventory-Service, and Billing-Service

## Overview

This project showcases the implementation of a **Microservices Architecture** with a focus on **Apache Kafka** for inter-service communication. The architecture consists of three independent microservices:

1. **Order-Service**: Produces Kafka messages with order details when an API endpoint is triggered.
2. **Inventory-Service**: Consumes Kafka messages and updates inventory based on orders.
3. **Billing-Service**: Consumes Kafka messages and creates billing records for each order.

Each microservice is designed to handle its responsibilities independently, ensuring scalability, modularity, and maintainability.

## Technologies Used

- **Spring Boot**
    - Spring Web
    - Spring Data JPA
    - Spring Kafka
- **Kafka** for message brokering
- **MySQL** as the database

---

## Services Overview

### 1. Order-Service

- **Responsibilities**:
    - Acts as a Kafka producer.
    - Exposes an endpoint to place orders.
    - Publishes order details as messages to the Kafka topic `order-topic`.
- **Dependencies**:
    - `spring-boot-starter-web`
    - `spring-boot-starter-data-jpa`
    - `spring-kafka`
- **Key Models**:
    - `Order`:
      ```java
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  
      private String productName;
      private int quantity;
      private double price;
      @Enumerated(EnumType.STRING)
      private PaymentStatus paymentStatus; // Set to COMPLETED when order is placed
      ```

### 2. Inventory-Service

- **Responsibilities**:
    - Acts as a Kafka consumer.
    - Updates the inventory table based on orders placed.
- **Dependencies**:
    - `spring-boot-starter-data-jpa`
    - `spring-kafka`
- **Key Models**:
    - `Inventory`:
      ```java
      @Id
      private Long id;
  
      private String productName;
      private int availableQuantity;
      private double price;
      ```

### 3. Billing-Service

- **Responsibilities**:
    - Acts as a Kafka consumer.
    - Saves billing records for each order in the billing table.
- **Dependencies**:
    - `spring-boot-starter-data-jpa`
    - `spring-kafka`
- **Key Models**:
    - `Billing`:
      ```java
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
  
      private Long orderId;
      private String productName;
      private int quantity;
      private double price;
      private LocalDateTime billingDate;
      ```

---

## Steps to Run the Project

### Prerequisites

1. Install **Kafka** and ensure it is running.
    - Start the Kafka broker and Zookeeper.
2. Install **Java 17** or higher.
3. Install **Maven** for dependency management.
4. Install **MySQL** and configure the database for each service.

### Running Each Service

1. **Order-Service**:

    - Navigate to the `order-service` directory.
    - Run:
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    - API Endpoint:
      ```
      POST http://localhost:8080/orders/place-order
      {
          "productName": "iPhone 14",
          "quantity": 1,
          "price": 999.99
      }
      ```

2. **Inventory-Service**:

    - Navigate to the `inventory-service` directory.
    - Run:
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    - Inventory gets updated automatically when an order is placed.

3. **Billing-Service**:

    - Navigate to the `billing-service` directory.
    - Run:
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    - Billing records are created automatically when an order is placed.

---

## Database Details

Each service uses MySQL as the database. Ensure the database and tables are created before running the services.

- **Order-Service**:

    - Table: `orders`
    - Schema: Automatically created when the application runs.

- **Inventory-Service**:

    - Table: `inventory`
    - Schema: Use the provided schema.sql file located in src/main/resources to create and populate the table.

- **Billing-Service**:

    - Table: `billing`
    - Schema: Automatically created when the application runs.

---

## Kafka Configuration

All Kafka properties are configured in the Java configuration class for each service. There is no need for Kafka-related settings in the `application.properties` file.

- **Topic**: `order-topic`
- **Producer**: Order-Service
- **Consumers**: Inventory-Service, Billing-Service

---

## Future Enhancements

1. Implement API Gateway for routing and authentication.
2. Add Swagger documentation for REST APIs.
3. Implement distributed tracing using Zipkin or Jaeger.
4. Add resilience patterns with Circuit Breakers using Resilience4j.

---

## Tools and Environment

- **Java**:
  ```
  java 17.0.1 2021-10-19 LTS
  Java(TM) SE Runtime Environment (build 17.0.1+12-LTS-39)
  Java HotSpot(TM) 64-Bit Server VM (build 17.0.1+12-LTS-39, mixed mode, sharing)
  ```
- **Maven**:
  ```
  Apache Maven 3.9.9
  ```
- **Kafka**:
  ```
  3.9.0
  ```
- **Database**: MySQL
- **OS**: macOS (Apple Silicon)

