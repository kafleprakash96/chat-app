# Chat Application with Spring Boot, Kafka, JPA, and Websocket

This document provides an overview of a chat application built using Spring Boot, Kafka, JPA, and Websocket.

## Technologies Used

* **Spring Boot:** A robust framework for Java applications, providing features like dependency injection, auto-configuration, and embedded servers.
* **Kafka:** A distributed streaming platform designed for high-throughput, fault-tolerant handling of real-time data streams.
* **JPA (Java Persistence API):** A standard interface for object-relational mapping, enabling seamless interaction with relational databases.
* **WebSocket:** A bidirectional communication protocol enabling real-time, full-duplex communication between a client and a server.

## Architecture

The application follows a layered architecture:

1. **Client:**
    * The user interface (e.g., web application, mobile app) through which users interact with the chat service.
    * Responsible for sending and receiving messages via WebSocket connections.

2. **WebSocket Server (Spring Boot):**
    * Handles WebSocket connections from clients.
    * Upon receiving a message, publishes it to a designated Kafka topic.

3. **Kafka Broker:**
    * The central message broker that receives messages from the WebSocket server.
    * Distributes messages to subscribed consumers.

4. **Message Consumer (Spring Boot):**
    * Subscribes to the Kafka topic.
    * Processes received messages (e.g., persisting them to the database).

5. **Database:**
    * Stores chat messages and other relevant data (e.g., user information).
    * Accessed by the Message Consumer through JPA.

## Key Features

* **Real-time messaging:** Enables instant message delivery between users.
* **Scalability:** Leverages Kafka for high-throughput message handling, enabling the system to scale horizontally.
* **Persistence:** Stores chat history for future reference and retrieval.
* **Reliable message delivery:** Ensures messages are not lost in case of network issues or server failures.

## Getting Started

1. **Prerequisites:**
    * Java 11 or later
    * Maven
    * A running Kafka broker (e.g., using Docker)
    * A relational database (e.g., PostgreSQL, MySQL)

2. **Clone the repository:**
   ```bash
   git clone https://github.com/kafleprakash96/chat-app.git
   cd chat-app
   ```
3. **Build the application**
    ```bash
   mvn clean install
    ```
   
4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
5. **Access the application**   
   Access application by visiting `localhost:8080`. Create chat room and begin chatting


## Demo

[![Demo Video](https://img.youtube.com/vi/cWgGQT8FBSI&t=5s/0.jpg)](https://www.youtube.com/watch?v=cWgGQT8FBSI&t=5s)
