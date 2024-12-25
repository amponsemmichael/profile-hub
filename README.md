# Profile-Hub

Profile-Hub is a microservices-based user management system designed to handle user authentication, profile management, and activity logging. This project is built using Spring Boot, Spring Cloud, and other related technologies.

## Table of Contents
- [Profile-Hub](#profile-hub)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Architecture](#architecture)
  - [Services](#services)
    - [Discovery Service](#discovery-service)
    - [User Authentication Service](#user-authentication-service)
    - [Profile Management Service](#profile-management-service)
    - [Activity Logging Service](#activity-logging-service)
    - [API Gateway](#api-gateway)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)

## Overview
Profile-Hub is designed to offer a scalable and maintainable user management solution leveraging microservices architecture. It allows for seamless user authentication, comprehensive profile management, and robust activity logging.

## Architecture
The system comprises several microservices, each handling specific aspects of user management:
- Discovery Service
- User Authentication Service
- Profile Management Service
- Activity Logging Service
- API Gateway

![Architecture Diagram](url_to_architecture_diagram)

## Services

### Discovery Service
The Discovery Service is responsible for managing the registration and discovery of microservices. It is built using Spring Cloud Netflix Eureka.

### User Authentication Service
The User Authentication Service handles user login, registration, password management, and JWT token generation/verification. It is secured using Spring Security and JWT.

### Profile Management Service
The Profile Management Service manages user profiles, including personal information, preferences, and settings. It uses Spring Data JPA for database interactions.

### Activity Logging Service
The Activity Logging Service tracks user activities, such as logins, profile updates, and other interactions within the system. It utilizes RabbitMQ for messaging.

### API Gateway
The API Gateway acts as a single entry point for all client requests and routes them to the appropriate microservice. It is implemented using Spring Cloud Gateway.

## Getting Started

### Prerequisites
- Java 11+
- Maven
- Docker
- PostgreSQL
- RabbitMQ

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/amponsemmichael/profile-hub.git

