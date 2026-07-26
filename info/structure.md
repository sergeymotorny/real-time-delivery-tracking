# Project Structure

The project is a multi-module monorepo with two independent Spring Boot services and shared Docker infrastructure.

```
real-time-delivery-tracking/
├── docker-compose.yml          # Kafka + Zookeeper infrastructure
├── UserApplication/            # Main web service (port 8081)
└── DriverApplication/          # Courier location producer (port 8082)
```

## UserApplication

The primary service. Handles users, orders, shipments, authentication, and real-time location consumption.

```
UserApplication/src/main/
├── java/com/motorny/
│   ├── controllers/            # Spring MVC @Controller classes (Thymeleaf views)
│   │   ├── AdminController     # /admin/** — admin-only management
│   │   ├── AuthController      # /auth/login, /auth/register
│   │   ├── CourierController   # /couriers/**
│   │   ├── OrderController     # /orders/**
│   │   ├── ShipmentController  # /shipments/**
│   │   ├── UserController      # /users/**
│   │   ├── MapController       # /map/**
│   │   └── HomeController      # /home
│   ├── service/                # Service interfaces
│   │   └── impl/               # Service implementations (@Service)
│   ├── repositories/           # Spring Data JPA repositories
│   ├── models/                 # JPA entities
│   │   └── enums/              # Domain enums (OrderStatus, ShipmentStatus, etc.)
│   ├── dto/                    # Data Transfer Objects
│   │   ├── admin/              # Admin-scoped DTOs
│   │   ├── courier/            # Courier-scoped DTOs
│   │   └── user/               # User auth/create/update DTOs
│   ├── mappers/                # MapStruct mapper interfaces
│   ├── exceptions/             # Custom exceptions + GlobalExceptionHandler
│   ├── security/               # SecurityConfig (filter chain, UserDetailsService)
│   ├── validation/             # Custom constraint annotations + validators
│   └── websocket/              # WebSocket config, handler, SessionManager
└── resources/
    ├── application.yml
    ├── static/                 # JS map clients (map-client.js, map-courier.js)
    └── templates/              # Thymeleaf HTML templates
        ├── admin/
        ├── auth/
        ├── blocks/             # Shared header/footer fragments
        ├── courier/
        └── user/
```

## DriverApplication

Lightweight producer service. Simulates courier GPS and publishes to Kafka.

```
DriverApplication/src/main/
├── java/com/motorny/
│   ├── controller/             # DeliveryLocationController — /location REST endpoints
│   ├── service/                # DeliveryLocationService — KafkaTemplate producer
│   ├── config/                 # KafkaConfig — topic definition
│   └── constant/               # AppConstant (topic name string)
└── resources/
    └── application.properties
```

## Architectural Conventions

- **Layering**: Controller → Service interface → ServiceImpl → Repository. Never skip layers.
- **DTOs**: Always use DTOs at the controller boundary; never expose JPA entities directly. Role-scoped DTOs go in subpackages (`admin/`, `courier/`, `user/`).
- **Mappers**: All entity↔DTO conversion uses MapStruct interfaces in `mappers/`. No manual mapping in service or controller code.
- **Exceptions**: Domain-specific exception classes in `exceptions/`. Centralized handling via `GlobalExceptionHandler` (`@ControllerAdvice`).
- **Enums**: Domain state values (status, type, method) live in `models/enums/` and are stored as `EnumType.STRING` in the database.
- **Lombok**: Use `@Data @Builder @AllArgsConstructor @NoArgsConstructor` on entities and DTOs. Use `@AllArgsConstructor` + field injection via constructor (not `@Autowired`) in Spring beans.
- **Security**: URL-level rules in `SecurityConfig`. Method-level rules via `@PreAuthorize` / `@EnableMethodSecurity`.
- **Kafka topic name**: Always reference via `AppConstant.DELIVERY_LOCATION`, never hard-code the string.
- **WebSocket**: Raw `TextWebSocketHandler` (not STOMP). Session tracking is managed by `SessionManager`.
