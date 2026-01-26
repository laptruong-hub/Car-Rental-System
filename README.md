🚗 Electric Car Rental System - Microservices Architecture
This project is a modern, scalable Electric Car Rental System built on a Microservices architecture. It is designed to handle high-concurrency authentication and complex rental workflows for both self-driving and chauffeur-driven services.
________________________________________
🌟 Core Features
The system is engineered with the following business modules:
•	Identity & Access Management (IAM): Advanced user management featuring Multi-session support via JWT and database-stored Refresh Tokens.
•	Flexible Rental Options: Support for both self-driving electric vehicles and chauffeur-driven services.
•	Smart Fleet Management: Includes an availability calendar, GPS tracking, and AI-powered chatbot assistance.
•	Complex Pricing & Insurance: Tiered pricing structures and customizable insurance options for different user needs.
•	Damage Reporting: Integrated module for reporting vehicle status and damages.
________________________________________
🛠 Tech Stack
The platform leverages the latest enterprise-grade technologies:
Component	Technology
Language	Java 21
Framework	Spring Boot 3.4.2
Database	PostgreSQL 15
Security	Spring Security, JJWT (0.11.5)
Service Discovery	Netflix Eureka
Documentation	Springdoc OpenAPI (Swagger)
Build Tool	Maven
________________________________________
🏗 Service Architecture
The system is decomposed into specialized microservices:
1.	iam-service (Port: 8080): The security gateway. It handles registration, login, token introspection, and multi-session management.
2.	eureka-server (Port: 8761): The central service registry and discovery hub.
3.	test-order-service: A service dedicated to order orchestration and data synchronization with the IAM module.
________________________________________
🚀 Getting Started
1. Prerequisites
•	JDK 21 or higher.
•	PostgreSQL instance running on port 5432.
•	Maven 3.x installed.
2. Database Configuration
Create a database named iam_db and update the application.yml in the iam-service folder:
YAML
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/iam_db
    username: your_username
    password: your_password
3. Running the Application
Bash
# Clone the repository
git clone https://github.com/your-repo/electric-car-rental.git

# Start the IAM Service
cd iam-service
mvn spring-boot:run
________________________________________
🔐 Security Framework
The system implements a robust security flow:
•	Dual-Token System: Uses short-lived Access Tokens for API calls and long-lived Refresh Tokens stored in PostgreSQL for session persistence.
•	Multi-session Support: Allows users to remain logged in across multiple devices (Laptop, Mobile, Tablet) simultaneously.
•	RBAC (Role-Based Access Control): Granular permissions for ADMIN, STAFF, DRIVER, and CUSTOMER roles.
________________________________________
📑 API Documentation
Interactive API documentation is available via Swagger UI once the service is running:
URL: http://localhost:8080/swagger-ui/index.html
________________________________________
[!IMPORTANT]
This project is currently under active development as a university graduation project.

