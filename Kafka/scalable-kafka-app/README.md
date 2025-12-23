# Scalable Kafka Microservices Application

A comprehensive microservices architecture built with Spring Boot, Spring Cloud Gateway, and Apache Kafka for event-driven communication.

## 🏗️ Architecture Overview

This project demonstrates a scalable microservices architecture with the following key components:

### Core Services

- **API Gateway**: Central entry point routing requests to microservices
- **User Service**: User management with event publishing
- **Order Service**: Order processing with event consumption/publishing
- **Inventory Service**: Inventory management with event-driven updates
- **Config Server**: Centralized configuration management (planned)

### Infrastructure

- **Apache Kafka**: Event streaming and inter-service communication
- **MySQL**: Persistent data storage for each service
- **Spring Cloud Gateway**: API routing and load balancing
- **Swagger/OpenAPI**: API documentation

## 📋 What's Been Implemented

### ✅ Completed Features

#### 1. API Gateway (`gateway/`)

- **Technology**: Spring Cloud Gateway with WebFlux
- **Port**: 8080
- **Routes Configured**:
  - `/api/users/**` → User Service (8081)
  - `/api/orders/**` → Order Service (8082)
  - `/api/inventory/**` → Inventory Service (8083)
- **Features**: Reactive routing, health checks via Actuator

#### 2. User Service (`services/user-service/`)

- **Port**: 8081
- **Database**: MySQL (`user_db`)
- **Domain Model**: User entity with JPA persistence
- **API Endpoints**:
  - `POST /api/users` - Create user
  - `GET /api/users` - List all users
  - `GET /api/users/{id}` - Get user by ID
  - `DELETE /api/users/{id}` - Delete user
- **Kafka Integration**:
  - **Publishes**: `user.created`, `user.deleted` events
  - **Event Format**: `{userId}:{email}` for created, `{userId}` for deleted
- **Additional Features**: Swagger documentation, JPA with Hibernate

#### 3. Order Service (`services/order-service/`)

- **Port**: 8082
- **Database**: MySQL (`order_db` - implied)
- **Domain Model**: Order entity with user reference
- **API Endpoints**:
  - `POST /api/orders` - Create order
  - `GET /api/orders` - List all orders
- **Kafka Integration**:
  - **Consumes**: `user.created` events (group: `order-service-group`)
  - **Publishes**: `order.created` events
  - **Event Format**: `{orderId}:{userId}`
- **Business Logic**: Processes user onboarding events

#### 4. Inventory Service (`services/inventory-service/`)

- **Port**: 8083
- **Database**: MySQL (`inventory_db` - implied)
- **Domain Model**: Inventory entity for product stock management
- **API Endpoints**:
  - `GET /api/inventory` - List all inventory items
  - `POST /api/inventory` - Add inventory item
- **Kafka Integration**:
  - **Consumes**: `order.created` events (group: `inventory-service-group`)
  - **Business Logic**: Stock reduction based on order events (planned)

#### 5. Project Structure

- **Multi-module Maven project** with parent POM
  - **Java Version**: 17
  - **Spring Boot**: 3.3.4
  - **Spring Cloud**: 2023.0.3
- **Shared Libraries Structure** (basic setup):
  - `shared-libs/common` - Common utilities
  - `shared-libs/security` - Security components
  - `shared-libs/messaging` - Messaging abstractions
  - `shared-libs/logging` - Logging utilities

#### 6. Event Flow Implementation

```
User Created → user.created event → Order Service (user onboarding)
Order Created → order.created event → Inventory Service (stock management)
User Deleted → user.deleted event → Cleanup processes
```

#### 7. Configuration Management

- **Environment-specific properties** for each service
- **Database configuration** with MySQL connections
- **Kafka configuration** with proper serializers/deserializers
- **Development settings** (auto-DDL, SQL logging)

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Apache Kafka 2.8+

### Running the Application

1. **Start Infrastructure**:

   ```bash
   # Start MySQL
   mysql.server start

   # Start Kafka & Zookeeper
   # (Follow Kafka installation guide)
   ```

2. **Build Project**:

   ```bash
   mvn clean install
   ```

3. **Start Services** (in order):

   ```bash
   # Terminal 1: User Service
   cd services/user-service && mvn spring-boot:run

   # Terminal 2: Order Service
   cd services/order-service && mvn spring-boot:run

   # Terminal 3: Inventory Service
   cd services/inventory-service && mvn spring-boot:run

   # Terminal 4: Gateway
   cd gateway && mvn spring-boot:run
   ```

4. **Access Services**:
   - API Gateway: http://localhost:8080
   - User Service: http://localhost:8081
   - Order Service: http://localhost:8082
   - Inventory Service: http://localhost:8083
   - Swagger UI: http://localhost:808{1,2,3}/swagger-ui.html

## 🔄 Event-Driven Workflow

1. **User Registration**: POST to `/api/users` via Gateway
2. **Event Published**: `user.created` event sent to Kafka
3. **Order Service**: Receives event, performs user onboarding logic
4. **Order Creation**: POST to `/api/orders` creates order
5. **Event Published**: `order.created` event sent to Kafka
6. **Inventory Service**: Receives event, updates stock levels

## 🎯 Improvements & Next Steps

### 🔧 Immediate Improvements Needed

#### 1. **Error Recovery & Resilience**

- [ ] Implement Circuit Breaker pattern (Resilience4j)
- [ ] Add retry mechanisms for Kafka consumers
- [ ] Implement dead letter queues for failed events
- [ ] Add health checks for all services

#### 2. **Configuration Management**

- [ ] Complete Config Server implementation
- [ ] Externalize environment-specific configs
- [ ] Add configuration profiles (dev, test, prod)
- [ ] Implement configuration encryption

#### 3. **Security Implementation**

- [ ] Add OAuth2/JWT authentication
- [ ] Implement API Gateway security filters
- [ ] Add service-to-service authentication
- [ ] Complete security shared library

#### 4. **Data Consistency**

- [ ] Implement proper event sourcing patterns
- [ ] Add transactional outbox pattern
- [ ] Implement saga pattern for distributed transactions
- [ ] Add event versioning and schema evolution

#### 5. **Monitoring & Observability**

- [ ] Add distributed tracing (Sleuth/Zipkin)
- [ ] Implement centralized logging (ELK stack)
- [ ] Add metrics collection (Micrometer/Prometheus)
- [ ] Complete logging shared library

### 🚀 Advanced Features to Implement

#### 1. **Event Sourcing & CQRS**

- [ ] Implement event store
- [ ] Add command and query separation
- [ ] Implement event replay capabilities
- [ ] Add snapshot mechanisms

#### 2. **Advanced Kafka Features**

- [ ] Implement Kafka Streams for complex event processing
- [ ] Add exactly-once processing semantics
- [ ] Implement event compaction strategies
- [ ] Add schema registry integration

#### 3. **Service Mesh Integration**

- [ ] Add Istio service mesh
- [ ] Implement traffic management
- [ ] Add security policies
- [ ] Implement canary deployments

#### 4. **Database Improvements**

- [ ] Implement database per service properly
- [ ] Add read replicas for query optimization
- [ ] Implement database migration strategies
- [ ] Add connection pooling optimization

#### 5. **Testing Strategy**

- [ ] Add comprehensive unit tests
- [ ] Implement integration tests
- [ ] Add contract testing (Pact)
- [ ] Implement end-to-end test automation

#### 6. **Deployment & DevOps**

- [ ] Containerize all services (Docker)
- [ ] Add Kubernetes deployment manifests
- [ ] Implement CI/CD pipelines
- [ ] Add infrastructure as code (Terraform)

#### 7. **Performance & Scalability**

- [ ] Implement caching strategies (Redis)
- [ ] Add horizontal scaling capabilities
- [ ] Implement load testing
- [ ] Add performance monitoring

### 🏗️ Shared Libraries Enhancement

#### 1. **Common Library**

- [ ] Add common DTOs and value objects
- [ ] Implement common exceptions
- [ ] Add utility classes
- [ ] Implement common validation

#### 2. **Messaging Library**

- [ ] Abstract Kafka operations
- [ ] Add message serialization/deserialization
- [ ] Implement retry and error handling
- [ ] Add message routing abstractions

#### 3. **Security Library**

- [ ] Add JWT token utilities
- [ ] Implement security filters
- [ ] Add role-based access control
- [ ] Implement audit logging

#### 4. **Logging Library**

- [ ] Add structured logging
- [ ] Implement correlation IDs
- [ ] Add log aggregation utilities
- [ ] Implement log levels management

## 📊 Current Service Ports

| Service           | Port | Database     | Purpose                            |
| ----------------- | ---- | ------------ | ---------------------------------- |
| Gateway           | 8080 | -            | API routing and load balancing     |
| User Service      | 8081 | user_db      | User management and authentication |
| Order Service     | 8082 | order_db     | Order processing and management    |
| Inventory Service | 8083 | inventory_db | Inventory and stock management     |

## 🧪 Testing the Implementation

### Sample API Calls

1. **Create a User**:

   ```bash
   curl -X POST http://localhost:8080/api/users \
     -H "Content-Type: application/json" \
     -d '{"username":"johndoe","email":"john@example.com","password":"password123"}'
   ```

2. **Create an Order**:

   ```bash
   curl -X POST http://localhost:8080/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId":1,"productName":"Laptop","quantity":1,"price":999.99}'
   ```

3. **Add Inventory**:
   ```bash
   curl -X POST http://localhost:8080/api/inventory \
     -H "Content-Type: application/json" \
     -d '{"productName":"Laptop","quantityAvailable":10}'
   ```

## 🔍 Known Issues

1. **POM Configuration**: Main POM has parsing issues that need resolution
2. **Database Schemas**: Need proper database initialization scripts
3. **Event Processing**: Kafka consumers need proper error handling
4. **Security**: No authentication/authorization implemented
5. **Configuration**: Hard-coded database credentials need externalization

## 📚 Technologies Used

- **Backend**: Spring Boot 3.3.4, Spring Cloud Gateway
- **Messaging**: Apache Kafka
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 17

---

This microservices architecture provides a solid foundation for building scalable, event-driven applications. The implemented event flow demonstrates proper separation of concerns and loose coupling between services.
