# Product Overview

Real-Time Delivery Tracking is a university diploma project — a web-based platform that lets customers place delivery orders and track their courier's location in real time on a map.

## Core Roles
- **Client** – creates and manages delivery orders, tracks live courier position
- **Courier** – picks up assigned orders, streams location updates
- **Admin** – manages all users and orders

## Key Capabilities
- Order lifecycle management (create, assign, track, complete)
- Real-time courier location broadcast via WebSocket
- Location data pipeline: DriverApplication → Kafka → UserApplication → browser clients
- Role-based access control (CLIENT / COURIER / ADMIN)
- Form-based authentication with Spring Security
