# AMALITECH GHANA
# CAPSTONE(LAB)- AGILE & DEVOPS 
**Instructor:** Emmanuel Aseber  
**Trainee:** Emmanuel Mensah Peprah

---

---

## Product Vision
To create a modern, scalable bookshop e-commerce platform that enables efficient inventory management, seamless product catalog browsing, and a complete customer shopping experience. The platform integrates a Spring Boot backend with RESTful and GraphQL APIs, a React frontend for user interaction, and MySQL for data persistence. Initially focusing on core catalog, inventory, and user management, the platform will evolve to include authentication, shopping cart functionality, order processing, payments, and comprehensive DevOps automation with CI/CD pipelines, monitoring, and logging.

---

## Codebase
**GitHub Repository:** 

**Technology Stack:**
- **Backend:** Spring Boot 4.0.2, Java 25, GraphQL, MySQL
- **Frontend:** React 18.2, React Router, Axios
- **Database:** MySQL 8.0+
- **Testing:** JUnit, Spring Boot Test, React Testing Library
- **Build Tools:** Maven, npm

**Current Project Structure:**
```
bookshop-ecommerce/
├── work/                          # Backend (Spring Boot)
│   ├── src/main/java/bookshop/
│   │   ├── controller/           # REST Controllers (Product, Inventory, User)
│   │   ├── services/             # Business logic layer
│   │   ├── dao/                  # Data Access Objects
│   │   ├── models/               # Entity models (Product, Inventory, Category, User)
│   │   ├── dto/                  # Request/Response DTOs
│   │   ├── exceptions/           # Custom exceptions
│   │   └── config/               # Configuration classes
│   └── src/test/java/bookshop/   # Unit and integration tests
├── frontend/                      # Frontend (React)
│   ├── src/
│   │   ├── components/           # Reusable components
│   │   ├── pages/                # Page components
│   │   ├── services/             # API client services
│   │   └── styles/               # CSS stylesheets
```
Implemented Features (Sprint 0)
•	Product CRUD operations with pagination, sorting, and filtering
•	Inventory management with stock reduction logic
•	User management with validation and unique email check
•	DTO pattern and custom exception handling
•	RESTful API design with proper HTTP status codes
•	MySQL schema with referential integrity
•	Initial unit and integration tests
Product Backlog (Backend Focused)
US1 – Complete Test Coverage
•	Unit tests for all service methods
•	Integration tests for controllers
•	Minimum 80% service layer coverage
US2 – Authentication & Authorization
•	BCrypt password hashing
•	JWT token generation and validation
•	Role-based access control (CUSTOMER, STAFF, ADMIN)
US4 – Order Management
•	Order and OrderItem models
•	Order placement endpoint
•	Inventory reduction on order creation
US5 – Low Stock Alert System
•	low_stock_threshold field
•	Endpoint to retrieve low-stock products
US6 – Inventory Dashboard Endpoint
•	Aggregate stock by status
•	Category filtering support
US7 – CI/CD Pipeline
•	GitHub Actions workflow
•	Automatic Maven test execution
US9 – Logging & Monitoring
•	Spring Boot Actuator endpoints
•	Structured logging with Logback
Definition of Done
•	All acceptance criteria met
•	Code reviewed and approved
•	80%+ coverage on new logic
•	Integration tests written
•	CI pipeline passing
•	Documentation updated
Sprint Roadmap
•	Sprint 1: Test Coverage & CI/CD
•	Sprint 2: Authentication, Cart & Monitoring
•	Sprint 3: Orders & Inventory Monitoring
Conclusion
This backend-focused roadmap ensures a secure, scalable, and production-ready system through strong testing practices, DevOps automation, authentication, and structured logging.
