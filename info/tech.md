# Tech Stack

## Language & Runtime
- Java 21
- Spring Boot 3.4.1 (both services)

## UserApplication Dependencies
- **Spring Web** – MVC controllers, REST endpoints
- **Spring Data JPA** + Hibernate – ORM, `ddl-auto: update`
- **PostgreSQL** (42.7.5) – primary database (`delivery_tracking` on port 5432)
- **Spring Security** – form-based auth, BCrypt password encoding, `@EnableMethodSecurity`
- **Spring WebSocket** – raw WebSocket (`TextWebSocketHandler`) for real-time location broadcast
- **Spring Kafka** – consumer (currently commented out in config; consumer group `user-group`)
- **Thymeleaf** + `thymeleaf-extras-springsecurity6` – server-side HTML templates
- **Lombok** (1.18.36) – `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`
- **MapStruct** (1.6.3) – interface-based DTO mappers, `componentModel = "spring"`
- **Spring Validation** – `@Valid`, custom constraint annotations
- **REST Assured**, **Mockito**, **spring-security-test** – testing

## DriverApplication Dependencies
- **Spring Web** – REST controller for location simulation
- **Spring Kafka** – producer (bootstrap: `localhost:9092`, String serializers)

## Infrastructure
- **Apache Kafka** + **Zookeeper** – via `bitnami/kafka` and `bitnami/zookeeper` Docker images
- Kafka topic: `delivery-location` (defined in `AppConstant.DELIVERY_LOCATION`)
- **Docker Compose** – starts Zookeeper (2181) and Kafka (9092)

## Ports
| Service | Port |
|---|---|
| UserApplication | 8081 |
| DriverApplication | 8082 |
| PostgreSQL | 5432 |
| Kafka | 9092 |
| Zookeeper | 2181 |

## Build System
Maven Wrapper (`mvnw`) — each service has its own `pom.xml`, built independently.

## Common Commands

```bash
# Start infrastructure (Kafka + Zookeeper)
docker-compose up -d

# Build a service (run from the service directory)
./mvnw clean package

# Run a service
./mvnw spring-boot:run

# Run tests
./mvnw test
```

On Windows use `mvnw.cmd` instead of `./mvnw`.
