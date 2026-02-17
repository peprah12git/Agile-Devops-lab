# Bookshop E-Commerce Platform

A full-stack e-commerce application for online bookshop management built with Spring Boot and React. The platform features comprehensive product catalog, inventory management, user management, and category organization with built-in logging and monitoring capabilities.

## Project Status

**Current Sprint:** Sprint 2 ✅ COMPLETED  
**Build Status:** passing  
**Test Coverage:** ~60% (service layer)  
**Last Updated:** February 17, 2026

## Technology Stack

### Backend
- **Framework:** Spring Boot 4.0.2
- **Language:** Java 25
- **Database:** MySQL 8.0+
- **Build Tool:** Maven
- **Monitoring:** Spring Boot Actuator
- **Logging:** Logback with file rotation
- **Testing:** JUnit 5, Mockito, Spring Boot Test

### Frontend
- **Framework:** React 18.2
- **Routing:** React Router 6.20
- **HTTP Client:** Axios 1.6
- **Build Tool:** npm

---

## Prerequisites

- **Java:** JDK 25 or higher
- **Maven:** 3.8+ (or use included Maven wrapper)
- **Node.js:** 18+ and npm 9+
- **MySQL:** 8.0+
- **Git:** For version control

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd work
```

### 2. Database Setup

**Create MySQL Database:**
```sql
CREATE DATABASE bookshop;
```

**Run Schema Script:**
```bash
mysql -u root -p bookshop < work/src/main/resources/schema.sql
```

### 3. Configure Database Connection

Edit `work/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookshop
    username: your_mysql_username
    password: your_mysql_password
```

**Environment-Specific Configs:**
- `application-dev.yml` - Development
- `application-test.yml` - Testing
- `application-prod.yml` - Production

### 4. Build and Run Backend

**Using Maven Wrapper (Recommended):**
```bash
cd work
./mvnw clean install
./mvnw spring-boot:run
```

**Using Maven:**
```bash
cd work
mvn clean install
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 5. Run Frontend

```bash
cd frontend
npm install
npm start
```

Frontend runs on: `http://localhost:3000`

---

## Running Tests

### Backend Tests

**Run All Tests:**
```bash
cd work
./mvnw test
```

**Run Specific Test Class:**
```bash
./mvnw test -Dtest=ProductControllerTest
```

**Generate Test Coverage Report:**
```bash
./mvnw test jacoco:report
```

View coverage report: `work/target/site/jacoco/index.html`

### Frontend Tests
```bash
cd frontend
npm test
```

---

## API Documentation

### Product Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | List all products (paginated) |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/search` | Search products with filters |

**Search Parameters:**
- `keyword` - Search by name
- `category` - Filter by category
- `minPrice` - Minimum price
- `maxPrice` - Maximum price
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)
- `sortBy` - Sort field (default: id)
- `sortDir` - Sort direction (ASC/DESC)

### Inventory Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/inventory` | Create inventory record |
| GET | `/api/inventory/product/{productId}` | Get inventory by product |
| PUT | `/api/inventory/{id}` | Update stock quantity |
| PUT | `/api/inventory/{id}/reduce` | Reduce stock |
| DELETE | `/api/inventory/{id}` | Delete inventory |

### User Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create new category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Monitoring & Health Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Application health status |
| GET | `/actuator/metrics` | Performance metrics |
| GET | `/actuator/info` | Application information |
| GET | `/actuator/loggers` | Logger configuration |

**Health Check Response Example:**
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "diskSpace": {"status": "UP"},
    "ping": {"status": "UP"}
  }
}
```

---

## Project Structure

```
bookshop-ecommerce/
├── work/                          # Backend (Spring Boot)
│   ├── src/main/java/bookshop/
│   │   ├── controller/           # REST Controllers
│   │   ├── services/             # Business logic
│   │   ├── dao/                  # Data Access Objects
│   │   ├── models/               # Entity models
│   │   ├── dto/                  # Request/Response DTOs
│   │   ├── exceptions/           # Custom exceptions
│   │   └── config/               # Configuration
│   ├── src/main/resources/
│   │   ├── application.yml       # Main config
│   │   ├── logback-spring.xml    # Logging configuration
│   │   └── schema.sql            # Database schema
│   └── src/test/java/bookshop/   # Tests
├── frontend/                      # Frontend (React)
│   ├── src/
│   │   ├── components/           # Reusable components
│   │   ├── pages/                # Page components
│   │   ├── services/             # API services
│   │   └── styles/               # CSS
├── logs/                          # Application logs (10MB rotation, 30-day retention)
└── .github/workflows/             # CI/CD pipelines
```

---

## Features

### ✅ Implemented

**Product Management:**
- Complete CRUD operations (Create, Read, Update, Delete)
- Advanced search with keyword matching
- Filter by category and price range
- Pagination and sorting (configurable page size, sort by any field)
- Product details with inventory tracking

**Inventory Management:**
- Stock quantity tracking
- Create and update inventory records
- Reduce stock operations (for order simulation)
- Get inventory by product ID
- Stock validation and constraints

**User Management:**
- User registration with email validation
- Email uniqueness enforcement
- Complete user CRUD operations
- User profile management

**Category Management:**
- Category CRUD operations
- Product categorization
- Category-based filtering

**Monitoring & Observability:**
- Spring Boot Actuator integration
- Health checks (database, disk space, application)
- Performance metrics and monitoring
- Comprehensive logging with Logback
- Log file rotation and retention policies
- Debug and info level logging

**Testing & CI/CD:**
- Comprehensive unit and integration tests
- GitHub Actions CI/CD pipeline
- Automated test execution on every commit
- JaCoCo test coverage reporting
- Automated build and artifact generation



---

## Logging

### Configuration

The application uses Logback for comprehensive logging with file rotation:

**Log Levels:**
- `DEBUG` for bookshop package (detailed application logs)
- `INFO` for root logger (general information)

**Log File:**
- Location: `logs/bookshop.log`
- Rotation: 10MB file size limit
- Retention: 30 days
- Pattern: `%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %level - %msg%n`

**Logged Operations:**
- All API requests and responses
- Database operations
- Error and exception details
- Business logic execution flow

**View Logs:**
```bash
# Tail live logs
tail -f logs/bookshop.log

# Windows PowerShell
Get-Content logs/bookshop.log -Wait -Tail 50
```

---

## Monitoring

### Health Checks

Check application status:
```bash
curl http://localhost:8080/actuator/health
```

**Components Monitored:**
- Database connectivity
- Disk space availability
- Application availability

### Metrics

View application metrics:
```bash
curl http://localhost:8080/actuator/metrics
```

**Available Metrics:**
- JVM memory usage
- HTTP request statistics
- Database connection pool
- Thread pool status
- CPU usage

---

## CI/CD Pipeline

### GitHub Actions Workflow

The project uses GitHub Actions for continuous integration:

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`

**Pipeline Steps:**
1. Checkout code
2. Set up JDK 25
3. Build with Maven
4. Run all tests
5. Generate test coverage report
6. Package application as JAR
7. Upload build artifacts

**View Pipeline:**
- Go to GitHub repository → Actions tab
- View test results and coverage reports

---

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running: `mysql -u root -p`
- Check credentials in `application.yml`
- Ensure database `bookshop` exists
- Check health endpoint: `curl http://localhost:8080/actuator/health`

### Port Already in Use
- Backend (8080): Change in `application.yml` → `server.port`
- Frontend (3000): Set `PORT=3001` before `npm start`

### Maven Build Fails
- Ensure JDK 25 is installed: `java -version`
- Clear Maven cache: `./mvnw clean`
- Update dependencies: `./mvnw dependency:resolve`

### Tests Failing
- Check test database configuration in `application-test.yml`
- Ensure test data is properly set up
- Run tests individually to isolate issues
- View test coverage: `work/target/site/jacoco/index.html`

### Logging Issues
- Check log file exists: `logs/bookshop.log`
- Verify Logback configuration: `src/main/resources/logback-spring.xml`
- Ensure logs directory has write permissions
- Check disk space for log rotation

### Monitoring/Actuator Not Working
- Verify Actuator dependency in `pom.xml`
- Check `application.yml` for actuator endpoints configuration
- Ensure application is running: `http://localhost:8080/actuator/health`
- Review logs for startup errors

---

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

---

## License

This project is for educational purposes.

---

## Contact

For questions or issues, please open a GitHub issue.
