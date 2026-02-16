# AMALITECH GHANA
# CAPSTONE(LAB)- AGILE & DEVOPS 
**Instructor:** Emmanuel Aseber  
**Trainee:** Emmanuel Mensah Peprah

---

## Table of Contents
1. [Product Vision](#product-vision)
2. [Codebase](#codebase)
3. [Sprint 0: Planning](#sprint-0-planning)
   - [Product Backlog](#product-backlog)
   - [User Stories](#user-stories)
   - [Definition of Done (DOD)](#definition-of-done-dod)
4. [Plan Sprint 1](#plan-sprint-1)
5. [Sprint 1: Execution](#sprint-1-execution)
6. [Sprint 1 Review](#sprint-1-review)
7. [Sprint 1 Retrospective](#sprint-1-retrospective)
8. [Plan Sprint 2](#plan-sprint-2)
9. [Sprint 2: Execution](#sprint-2-execution)
10. [Sprint 2 Review](#sprint-2-review)
11. [Sprint 2 Retrospective](#sprint-2-retrospective)
12. [Plan Sprint 3](#plan-sprint-3)

---

## Product Vision
To create a modern, scalable bookshop e-commerce platform that enables efficient inventory management, seamless product catalog browsing, and a complete customer shopping experience. The platform integrates a Spring Boot backend with RESTful and GraphQL APIs, a React frontend for user interaction, and MySQL for data persistence. Initially focusing on core catalog, inventory, and user management, the platform will evolve to include authentication, shopping cart functionality, order processing, payments, and comprehensive DevOps automation with CI/CD pipelines, monitoring, and logging.

---

## Codebase
**GitHub Repository:** [Your Repository Link Here]

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

---

## Sprint 0: Planning & Initial Implementation

### Current State Assessment (What's Already Built)

**✅ Implemented Features:**

**Backend (Spring Boot):**
- ✅ Product CRUD operations (Create, Read, Update, Delete)
- ✅ Advanced product querying:
  - Pagination and sorting (configurable page size, sort by any field, ASC/DESC)
  - Search by keyword (name matching)
  - Filter by category
  - Filter by price range (min/max)
- ✅ Inventory management:
  - Create inventory records
  - Update stock quantities
  - Get inventory by product ID
  - Reduce stock (for order simulation)
  - Delete inventory
- ✅ User management:
  - User registration with validation
  - Email uniqueness check
  - Get all users
  - Get user by ID
  - Update user information
  - Delete user
- ✅ Category management (referenced in schema)
- ✅ RESTful API design with proper HTTP status codes
- ✅ DTO pattern (Request/Response DTOs)
- ✅ Custom exception handling (ProductNotFoundException, InventoryNotFoundException, EmailAlreadyExistsException, etc.)
- ✅ Database schema with referential integrity constraints
- ✅ Input validation using Jakarta Validation annotations

**Frontend (React):**
- ✅ Complete routing setup (React Router)
- ✅ Pages implemented:
  - HomePage - Landing page
  - ProductsPage - Browse products with search, filter, sort, pagination
  - ProductDetailPage - View individual product details
  - InventoryPage - Manage inventory (admin view)
  - UsersPage - View and manage users
- ✅ Reusable components:
  - ProductCard - Display product information
  - SearchBar - Search functionality with input handling
  - Pagination - Navigate through pages
  - LoadingSpinner - Loading state indicator
  - ErrorMessage - Display errors to users
  - Navbar - Navigation menu
- ✅ API integration with backend via Axios
- ✅ Proxy configuration to backend (localhost:8080)
- ✅ CSS styling for all components and pages

**Testing:**
- ✅ Comprehensive ProductController integration tests (769 lines covering all endpoints)
- ✅ ProductService unit tests (createProduct functionality)
- ✅ InventoryService unit tests (reduceStock functionality)
- ✅ UserService unit tests (registerUser functionality)
- ✅ MockMvc setup for controller testing
- ✅ Mockito for service layer mocking

**Database:**
- ✅ MySQL schema with tables: products, inventory, category, users
- ✅ Foreign key relationships
- ✅ Indexes for performance optimization
- ✅ Check constraints (e.g., quantity >= 0)
- ✅ Sample data for testing

---

### ❌ Gaps Identified (To Be Implemented in Future Sprints)

**Security & Access Control:**
- ❌ No authentication system (no login/logout)
- ❌ No authorization (all endpoints are public)
- ❌ No password encryption (users table has no password field)
- ❌ No JWT tokens or session management
- ❌ No role-based access control (ADMIN, STAFF, CUSTOMER roles)

**E-Commerce Features:**
- ❌ No shopping cart functionality
- ❌ No order management system
- ❌ No checkout process
- ❌ No payment integration

**Inventory Enhancements:**
- ❌ No low-stock alerts or warnings
- ❌ No inventory dashboard with visual indicators
- ❌ No automatic stock threshold notifications

**DevOps & Quality:**
- ❌ No CI/CD pipeline (no GitHub Actions or Jenkins)
- ❌ No automated deployment
- ❌ No logging framework configured (no SLF4J/Logback setup)
- ❌ No monitoring (no Spring Boot Actuator endpoints)
- ❌ No health checks or metrics
- ❌ Test coverage incomplete (missing tests for many service methods)
- ❌ No frontend tests (React Testing Library not set up)

**API & Advanced Features:**
- ❌ GraphQL not implemented (dependency present but no schema/resolvers)
- ❌ No API documentation (no Swagger/OpenAPI)
- ❌ No rate limiting or API security

**Frontend Enhancements:**
- ❌ No advanced error handling (no error boundary)
- ❌ No toast notifications for user feedback
- ❌ No form validation feedback
- ❌ No 404 page for invalid routes
- ❌ No product images (image management not implemented)
- ❌ No category filter dropdown in UI

---

### Technology Stack Summary
- **Backend:** Spring Boot 4.0.2, Java 25
- **Frontend:** React 18.2, React Router 6.20, Axios 1.6
- **Database:** MySQL (configured in application.yml)
- **Build Tools:** Maven (backend), npm (frontend)
- **Testing:** JUnit, Mockito, Spring Boot Test, MockMvc

---

### Product Backlog

#### User Stories

##### **US1 - Complete Test Coverage for All Services**
**As a** developer  
**I want to** achieve comprehensive test coverage across all service and controller layers  
**So that** I can ensure code quality, prevent bugs, and enable safe refactoring

**Acceptance Criteria:**
- Unit tests for all ProductService methods (not just createProduct)
- Unit tests for all InventoryService methods (not just reduceStock)
- Unit tests for all UserService methods (not just registerUser)
- Integration tests for InventoryController endpoints
- Integration tests for UserController endpoints
- Test coverage reaches at least 80% across service layer
- All tests pass successfully
- Mock external dependencies properly

**Priority:** High  
**Story Points:** 5

**Current State:** Only 4 test files exist with limited coverage

---

##### **US2 - Implement Authentication and Authorization System**
**As a** system administrator  
**I want** users to log in with secure credentials and have role-based access control  
**So that** only authorized users can perform administrative actions on inventory and products

**Acceptance Criteria:**
- Add password field to User model and database schema
- Users can register with email and password (extend existing registration)
- Passwords are hashed using BCrypt before storage
- Users can log in with email/password and receive a JWT token
- Three roles implemented: CUSTOMER, STAFF, ADMIN
- JWT token validates user identity on protected endpoints
- Product and Inventory management endpoints protected (ADMIN/STAFF only)
- User browsing endpoints remain public or require CUSTOMER role
- Frontend stores JWT token and includes in API requests
- Login/Logout UI components created
- Protected routes implemented in React (redirect to login if unauthenticated)

**Priority:** High  
**Story Points:** 8

**Current State:** User registration exists but no authentication/password management

---

##### **US3 - Implement Shopping Cart Functionality**
**As a** customer  
**I want** to add books to a shopping cart  
**So that** I can review my selections before purchasing

**Acceptance Criteria:**
- Create Cart and CartItem database tables
- Create Cart and CartItem models in backend
- Implement CartDao and CartService
- Create CartController with endpoints:
  - POST /api/cart/add - Add item to cart
  - PUT /api/cart/update/{itemId} - Update quantity
  - DELETE /api/cart/remove/{itemId} - Remove item
  - GET /api/cart - Get user's cart
  - DELETE /api/cart/clear - Clear cart
- Customers can add products to cart with specified quantity
- Cart displays product details (name, price, quantity, subtotal)
- Customers can update quantities or remove items from cart
- Cart shows total price calculation
- Cart persists in database (associated with user ID)
- Validation: Cannot add out-of-stock items to cart
- Validation: Cannot add quantity exceeding available stock
- Frontend CartPage component created
- "Add to Cart" button on ProductCard and ProductDetailPage
- Cart icon in Navbar shows item count
- Unit and integration tests for cart functionality

**Priority:** High  
**Story Points:** 5

**Current State:** No cart functionality exists

---

##### **US4 - Implement Order Management System**
**As a** customer  
**I want** to place orders from my shopping cart  
**So that** I can complete my purchase

**Acceptance Criteria:**
- Customers can place an order from cart
- Orders capture customer info, items, quantities, and total price
- Order reduces inventory stock automatically
- Order confirmation is generated with order ID and details
- Customers can view order history
- Admins can view all orders and update order status

**Priority:** High  
**Story Points:** 8

---

##### **US5 - Low Stock Alert System**
**As a** bookshop administrator  
**I want** to receive alerts when stock levels are low  
**So that** I can restock before running out

**Acceptance Criteria:**
- Admin can set a low-stock threshold per product
- Products below threshold display a "Low Stock" badge
- Admin dashboard shows all low-stock items
- Email notifications sent when stock falls below threshold (optional enhancement)

**Priority:** Medium  
**Story Points:** 3

---

##### **US6 - Inventory Dashboard with Filters**
**As a** bookshop administrator  
**I want** to view an inventory dashboard with visual indicators  
**So that** I can quickly assess stock status

**Acceptance Criteria:**
- Dashboard shows products grouped by stock status (In Stock, Low Stock, Out of Stock)
- Visual indicators use color coding (green, yellow, red)
- Dashboard allows filtering by category
- Real-time updates when inventory changes
- Export inventory report to CSV (optional enhancement)

**Priority:** Medium  
**Story Points:** 5

---

##### **US7 - Setup CI/CD Pipeline with GitHub Actions**
**As a** developer  
**I want** an automated CI/CD pipeline  
**So that** code is automatically tested and built on every commit

**Acceptance Criteria:**
- Create .github/workflows/ci.yml in repository
- Pipeline triggers on push and pull requests to main branch
- Pipeline runs Maven tests for backend
- Pipeline builds frontend with npm
- Build status reported to GitHub (pass/fail)
- Add build status badge to README.md
- Pipeline fails if any tests fail
- Pipeline artifacts stored (optional)

**Priority:** High  
**Story Points:** 3

**Current State:** No CI/CD pipeline exists

---

##### **US8 - Enhance Frontend with Category Filter and Error Handling**
**As a** customer  
**I want** to filter products by category and see helpful error messages  
**So that** I can find products easier and understand what went wrong when errors occur

**Acceptance Criteria:**
- Category filter dropdown added to ProductsPage
- Dropdown populated with categories from API
- Filtering by category works with pagination and search
- Create ErrorBoundary component to catch React errors
- Display user-friendly error messages for network failures
- Create custom 404 Not Found page component
- Add 404 route in React Router
- Inline validation errors on forms (registration, product creation)
- Loading states prevent duplicate form submissions

**Priority:** Medium  
**Story Points:** 3

**Current State:** Category filtering exists in backend but no UI dropdown; basic error handling present

---

##### **US9 - Implement Logging and Monitoring with Spring Boot Actuator**
**As a** developer  
**I want** structured logging and application health monitoring  
**So that** I can troubleshoot issues and monitor application performance

**Acceptance Criteria:**
- Add Spring Boot Actuator dependency to pom.xml
- Configure Logback for structured logging
- Add SLF4J logging statements to:
  - All controller methods (request/response logging)
  - All service methods (business logic operations)
  - Exception handlers (error logging)
  - Database operations (DAO layer)
- Logs include: timestamp, log level (INFO, WARN, ERROR), class name, message
- Configure log output to both console and file (logs/bookshop.log)
- Enable health check endpoint: /actuator/health
- Enable metrics endpoint: /actuator/metrics
- Configure application.yml to expose actuator endpoints
- Test logging in various scenarios (success, errors, edge cases)
- Document logging setup in README

**Priority:** Medium  
**Story Points:** 3

**Current State:** No logging framework configured; no actuator endpoints

---

##### **US10 - Implement GraphQL API for Products**
**As a** frontend developer  
**I want** a GraphQL API endpoint  
**So that** I can query exactly the product data I need with one request

**Acceptance Criteria:**
- Create GraphQL schema (.graphqls file) defining:
  - Product type
  - Category type
  - Inventory type
- Define GraphQL queries:
  - products(page: Int, size: Int): [Product]
  - product(id: Int!): Product
  - categories: [Category]
- Define GraphQL mutations:
  - createProduct(input: ProductInput!): Product
  - updateProduct(id: Int!, input: ProductInput!): Product
- Implement GraphQL resolvers/controllers
- GraphQL endpoint accessible at /graphql
- GraphQL Playground/GraphiQL enabled for testing
- Documentation of GraphQL schema in README
- Optionally update frontend to use GraphQL (or keep REST)

**Priority:** Low  
**Story Points:** 5

**Current State:** spring-boot-starter-graphql dependency added but no schema/resolvers implemented

---

##### **US11 - Inventory Dashboard with Visual Indicators**
**As a** bookshop administrator  
**I want** to view an inventory dashboard with color-coded stock status  
**So that** I can quickly identify stock issues

**Acceptance Criteria:**
- Create backend endpoint: GET /api/inventory/dashboard
- Dashboard aggregates products by status:
  - In Stock (quantity > threshold)
  - Low Stock (quantity <= threshold but > 0)
  - Out of Stock (quantity = 0)
- Add lowStockThreshold field to Inventory table (default: 10)
- Support category filtering in dashboard
- Frontend InventoryDashboard component created
- Visual color indicators:
  - Green badges for "In Stock"
  - Yellow badges for "Low Stock"
  - Red badges for "Out of Stock"
- Display count for each status category
- Category filter dropdown in dashboard UI
- Real-time updates when inventory changes

**Priority:** Medium  
**Story Points:** 5

**Current State:** Basic InventoryPage exists but no dashboard view or visual indicators

---

##### **US12 - Product Image Management**
**As a** bookshop administrator  
**I want** to upload and display product images  
**So that** customers can see what books look like

**Acceptance Criteria:**
- Admin can upload product images
- Images stored on server or cloud storage
- Product cards display thumbnail images
- Product detail page shows larger images
- Default placeholder image for products without images
- Image validation (file type, size limits)

**Priority:** Medium  
**Story Points:** 5

---

### Definition of Done (DOD)

A user story is considered **Done** when:

1. **Code Quality**
   - Code follows Java and JavaScript best practices
   - Code reviewed and approved by at least one peer
   - No code smells or critical SonarQube issues
   - Proper naming conventions and code documentation

2. **Functionality**
   - All acceptance criteria are met
   - Feature works as expected in both backend and frontend
   - Edge cases and error scenarios handled appropriately

3. **Testing**
   - Unit tests written and passing (minimum 80% coverage for new code)
   - Integration tests written for API endpoints
   - Manual testing completed and verified
   - No regression in existing functionality

4. **Documentation**
   - Code comments added where necessary
   - API documentation updated (if applicable)
   - README updated with new features or setup steps

5. **Deployment**
   - Feature merged to main branch
   - Successfully deployed to staging/development environment
   - Database migrations applied (if applicable)

6. **Approval**
   - Product Owner or instructor has reviewed and accepted the feature
   - Demo completed (if required)

7. **No Critical Bugs**
   - No blocker or critical bugs remain open
   - Known minor issues documented in backlog

---

## Plan Sprint 1

### Sprint 1 Goal
**Establish a strong foundation by achieving comprehensive test coverage, setting up CI/CD automation, and enhancing the frontend with better filtering and error handling.**

### Sprint 1 Duration
**1 week (Simulated)**

### Sprint 1 Backlog (Selected User Stories)
The following high-priority user stories are selected for Sprint 1:

1. **US1 - Complete Test Coverage for All Services** (5 points)
2. **US7 - Setup CI/CD Pipeline with GitHub Actions** (3 points)
3. **US8 - Enhance Frontend with Category Filter and Error Handling** (3 points)

**Total Story Points:** 11

**Rationale:** Before adding complex features like authentication and shopping cart, we need a solid foundation of automated testing and CI/CD to ensure quality. Frontend enhancements will improve user experience with existing features.

### Sprint 1 Tasks Breakdown

#### US1: Complete Test Coverage for All Services
- [ ] Write unit tests for remaining ProductService methods:
  - getAllProducts()
  - getProductsByCategory()
  - searchProducts()
  - getProductsByPriceRange()
  - updateProduct()
  - deleteProduct()
- [ ] Write unit tests for remaining InventoryService methods:
  - createInventory()
  - getInventoryByProductId()
  - updateQuantity()
  - addStock()
  - deleteInventory()
- [ ] Write unit tests for remaining UserService methods:
  - getAllUsers()
  - getUserById()
  - updateUser()
  - deleteUser()
  - emailExists()
- [ ] Create integration tests for InventoryController (following ProductController test pattern)
- [ ] Create integration tests for UserController
- [ ] Run code coverage report and verify 80%+ coverage
- [ ] Document testing approach in README

#### US7: CI/CD Pipeline Setup
- [ ] Create `.github/workflows` directory
- [ ] Create `ci.yml` GitHub Actions workflow file
- [ ] Configure Java 25 environment setup
- [ ] Configure Maven test execution
- [ ] Configure Node.js environment setup
- [ ] Configure npm install and build for frontend
- [ ] Set up workflow to run on push and pull requests to main
- [ ] Test pipeline with a sample commit
- [ ] Add build status badge to README.md
- [ ] Document CI/CD setup in README

#### US8: Frontend Enhancement - Category Filter and Error Handling
- [ ] Create CategoryService.js to fetch categories from backend
- [ ] Add category dropdown component to ProductsPage
- [ ] Integrate category filter with existing search and pagination
- [ ] Create ErrorBoundary.js component
- [ ] Wrap App component with ErrorBoundary
- [ ] Create NotFoundPage.js for 404 errors
- [ ] Add 404 route to React Router configuration
- [ ] Enhance form validation messages in UsersPage (registration)
- [ ] Add loading state management to prevent double submissions
- [ ] Test error scenarios (network errors, invalid routes)

---

## Sprint 1: Execution

### Daily Progress Log (To Be Completed During Sprint)

#### Day 1: Planning and Test Setup
- Review sprint backlog and acceptance criteria
- Set up Git feature branches for each user story
- Review existing ProductControllerTest as template for new tests
- Begin writing unit tests for ProductService methods

#### Day 2: Complete Service Layer Tests
- Complete unit tests for all ProductService methods
- Write unit tests for all InventoryService methods
- Write unit tests for all UserService methods
- Run coverage reports to track progress toward 80% goal

#### Day 3: Integration Tests and CI/CD Setup
- Create InventoryController integration tests using MockMvc
- Create UserController integration tests
- Create `.github/workflows/ci.yml` file
- Configure pipeline for Maven and npm builds

#### Day 4: Frontend Enhancement
- Create CategoryService and implement category API integration
- Add category filter dropdown to ProductsPage
- Create ErrorBoundary and NotFoundPage components
- Implement enhanced form validation

#### Day 5: Testing, Documentation, and Review
- Test CI/CD pipeline with sample commits
- Verify all acceptance criteria met
- Update README with testing and CI/CD documentation
- Add build status badge to README
- Code review and merge to main branch
- Conduct Sprint 1 review meeting

---

## Sprint 1 Review (To Be Conducted at Sprint End)

### Objective
Deliver a well-tested, automated platform foundation with comprehensive test coverage and CI/CD pipeline to enable safe, rapid development in future sprints.

### Expected Deliverables

#### **US1 – Complete Test Coverage for All Services**
**Expected Outcome:**
- Comprehensive unit tests for all ProductService, InventoryService, and UserService methods
- Integration tests for InventoryController and UserController matching ProductController test quality
- Test coverage report showing 80%+ coverage across service layer
- All tests passing in local development

**Expected Outcome:**
- Comprehensive unit tests for all ProductService, InventoryService, and UserService methods
- Integration tests for InventoryController and UserController matching ProductController test quality
- Test coverage report showing 80%+ coverage across service layer
- All tests passing in local development

**Success Metrics:**
- Number of test files: At least 6 new test files created
- Number of test cases: 100+ test methods across all test suites
- Coverage percentage: ≥80%
- Build time: All tests complete in under 5 minutes

---

#### **US7 – Setup CI/CD Pipeline with GitHub Actions**
**Expected Outcome:**
- Automated GitHub Actions workflow running on every push/PR
- Pipeline executes all Maven tests for backend
- Pipeline builds frontend with npm
- Build status visible in GitHub Actions tab
- Build badge displayed in README
- Failed tests prevent merge

**Pipeline Configuration:**
```yaml
# Expected workflow structure
name: CI/CD Pipeline
on: [push, pull_request]
jobs:
  build:
    - Set up JDK 25
    - Run Maven tests
    - Set up Node.js
    - Run npm build
    - Report results
```

---

#### **US8 – Enhance Frontend with Category Filter and Error Handling**
**Expected Outcome:**
- Category dropdown populated from `/api/categories` endpoint
- Category filter works alongside search and pagination
- ErrorBoundary catches and displays React errors gracefully
- Custom 404 page for invalid routes
- Form validation shows inline error messages
- Loading states prevent duplicate submissions

**UI Improvements:**
- Better user feedback on errors
- No blank screens when errors occur
- Clearer navigation when routes don't exist

---

### Demo Plan
1. **Run test suite** - Show 80%+ coverage and all passing tests
2. **Trigger CI/CD pipeline** - Make a commit and show GitHub Actions running
3. **Test category filter** - Filter products by different categories
4. **Test error handling** - Navigate to invalid route, trigger network error
5. **Show build status badge** - Display in README

---

## Sprint 1 Retrospective (To Be Conducted at Sprint End)

### Objective
Reflect on Sprint 1 execution, identify what worked well, challenges faced, and actionable improvements for Sprint 2.

### What Went Well (Expected Positives)
- ✅ Comprehensive test coverage provides confidence for future changes
- ✅ CI/CD pipeline automates quality checks
- ✅ ProductController tests serve as excellent template for other controller tests
- ✅ Category filter enhances product browsing experience
- ✅ Error handling prevents bad user experience

### Potential Challenges / Areas to Watch
- ⚠️ Test execution time may increase with more tests - monitor performance
- ⚠️ CI/CD pipeline may need optimization if builds take too long
- ⚠️ Coordination between backend and frontend features requires clear communication
- ⚠️ Maintaining test code quality as important as production code

### Action Items for Sprint 2
- [ ] Monitor CI/CD pipeline execution time and optimize if > 5 minutes
- [ ] Consider parallel test execution if test suite grows large
- [ ] Document testing patterns and best practices for consistency
- [ ] Set up code coverage reporting tool (Codecov or JaCoCo)

### Team Morale & Readiness
**Expected Morale:** High - solid foundation established for future development  
**Confidence Level:** Strong - automated tests and CI/CD provide safety net  
**Sprint 2 Readiness:** Ready to tackle authentication and more complex features

---

## Plan Sprint 2

### Sprint 2 Goal
**Implement secure authentication with role-based access control (RBAC), develop shopping cart functionality, and add logging/monitoring capabilities to enable e-commerce workflows and improve observability.**

### Sprint 2 Duration
**1 week (Simulated)**

### Sprint 2 Backlog (Selected User Stories)
The following high-priority user stories are selected for Sprint 2:

1. **US2 - Implement Authentication and Authorization System** (8 points)
2. **US3 - Implement Shopping Cart Functionality** (5 points)
3. **US9 - Implement Logging and Monitoring with Spring Boot Actuator** (3 points)

**Total Story Points:** 16

**Rationale:** With a solid testing and CI/CD foundation from Sprint 1, Sprint 2 focuses on enabling secure access control and the core e-commerce feature (shopping cart). Logging and monitoring will provide visibility into the new features.

### Sprint 2 Tasks Breakdown

#### US2: Authentication and Authorization System
**Backend Tasks:**
- [ ] Add Spring Security dependency to pom.xml
- [ ] Add JWT dependency (io.jsonwebtoken:jjwt)
- [ ] Add password and role fields to User model
- [ ] Update users table schema (add password VARCHAR(255), role VARCHAR(20))
- [ ] Create JwtUtil class for token generation and validation
- [ ] Create UserDetailsServiceImpl implementing Spring Security UserDetailsService
- [ ] Create AuthenticationController with endpoints:
  - POST /api/auth/register (enhanced user registration with password)
  - POST /api/auth/login (returns JWT token)
- [ ] Create JwtRequestFilter to validate tokens on each request
- [ ] Configure Spring Security (WebSecurityConfig)
- [ ] Add @PreAuthorize annotations to protect endpoints:
  - ProductController: create/update/delete require ADMIN or STAFF
  - InventoryController: all operations require ADMIN or STAFF
  - UserController: sensitive operations require ADMIN
- [ ] Implement BCrypt password encoding
- [ ] Write unit tests for authentication logic
- [ ] Write integration tests for auth endpoints

**Frontend Tasks:**
- [ ] Create AuthService.js for login/register API calls
- [ ] Create Login.js component with form
- [ ] Create Register.js component (enhance existing user registration)
- [ ] Update Navbar to show login/logout based on auth state
- [ ] Create AuthContext for global auth state management
- [ ] Store JWT token in localStorage
- [ ] Create axios interceptor to add Authorization header to requests
- [ ] Implement protected routes (redirect to login if not authenticated)
- [ ] Add logout functionality (clear token and redirect)
- [ ] Test authentication flow end-to-end

#### US3: Shopping Cart Functionality
**Backend Tasks:**
- [ ] Create cart and cart_items tables in schema.sql:
  - cart: cart_id, user_id, created_at
  - cart_items: item_id, cart_id, product_id, quantity, added_at
- [ ] Create Cart and CartItem models
- [ ] Create CartDao and CartItemDao interfaces and implementations
- [ ] Create CartService with methods:
  - addItemToCart(userId, productId, quantity)
  - updateCartItemQuantity(itemId, quantity)
  - removeCartItem(itemId)
  - getCartByUserId(userId)
  - clearCart(userId)
  - calculateCartTotal(cartId)
- [ ] Create CartController with endpoints:
  - POST /api/cart/add
  - PUT /api/cart/items/{itemId}
  - DELETE /api/cart/items/{itemId}
  - GET /api/cart
  - DELETE /api/cart/clear
- [ ] Implement cart validation (stock availability checks)
- [ ] Write unit tests for CartService
- [ ] Write integration tests for CartController

**Frontend Tasks:**
- [ ] Create CartService.js for cart API calls
- [ ] Create CartPage.js component
- [ ] Create CartItem.js component
- [ ] Add "Add to Cart" button to ProductCard component
- [ ] Add "Add to Cart" functionality to ProductDetailPage
- [ ] Add cart icon with item count badge to Navbar
- [ ] Implement cart total calculation display
- [ ] Implement remove item functionality
- [ ] Implement update quantity functionality
- [ ] Add cart state management (Context API or local state)
- [ ] Test cart workflows (add, update, remove, clear)

#### US9: Logging and Monitoring
- [ ] Add spring-boot-starter-actuator to pom.xml (if not already present)
- [ ] Configure application.yml to enable actuator endpoints:
  ```yaml
  management:
    endpoints:
      web:
        exposure:
          include: health, metrics, info
  ```
- [ ] Configure Logback in logback-spring.xml:
  - Console appender for development
  - File appender with rolling policy (logs/bookshop.log)
  - Log pattern with timestamp, level, thread, class, message
- [ ] Add SLF4J Logger instances to all controllers:
  - Log incoming requests (method, endpoint, parameters)
  - Log outgoing responses (status, execution time)
- [ ] Add logging to all service methods:
  - Log business operations (created product, updated inventory, etc.)
  - Log validation failures and business exceptions
- [ ] Add logging to DAO layer (optional, for debugging)
- [ ] Test health endpoint: GET /actuator/health
- [ ] Test metrics endpoint: GET /actuator/metrics
- [ ] Document actuator endpoints in README
- [ ] Test logging in various scenarios

---

## Sprint 2: Execution (To Be Completed During Sprint)

### Daily Progress Log

#### Day 1: Authentication Backend Setup
- Add Spring Security and JWT dependencies
- Design authentication database schema changes
- Update User model with password and role fields
- Create JwtUtil class for token operations
- Begin implementing UserDetailsService

#### Day 2: Complete Authentication Backend
- Create AuthenticationController with register/login endpoints
- Implement JwtRequestFilter for token validation
- Configure Spring Security (WebSecurityConfig)
- Add @PreAuthorize annotations to protect sensitive endpoints
- Test authentication endpoints with Postman/Insomnia

#### Day 3: Authentication Frontend & Shopping Cart Backend Start
- Create frontend Login and Register components
- Implement AuthContext and token storage
- Create axios interceptor for Authorization header
- Implement protected routes in React
- Begin shopping cart backend (create tables, models)

#### Day 4: Complete Shopping Cart
- Finish CartService and CartDao implementation
- Create CartController with all endpoints
- Build CartPage component in React
- Add "Add to Cart" buttons to product views
- Implement cart icon with item count in Navbar
- Write cart validation logic

#### Day 5: Logging, Testing, and Integration
- Configure Spring Boot Actuator
- Set up Logback with file and console appenders
- Add logging statements throughout application
- Write comprehensive tests for auth and cart features
- End-to-end integration testing
- Update README with new features documentation
- Sprint review and retrospective

---

## Sprint 2 Review (To Be Conducted at Sprint End)

### Objective
Deliver a secure, authenticated platform with role-based access control and functional shopping cart to enable controlled access and e-commerce transactions.

### Expected Deliverables

#### **US2 – Authentication and Authorization System**
**Expected Outcome:**
- Users can register with email, password (securely hashed with BCrypt)
- Users can log in and receive JWT token
- Three roles implemented: CUSTOMER, STAFF, ADMIN
- Protected endpoints enforce authorization:
  - Product/Inventory create/update/delete: ADMIN or STAFF only
  - Cart operations: Authenticated users only
  - User management: ADMIN only
- Frontend login/logout fully functional
- JWT token stored securely in localStorage
- Protected routes redirect unauthenticated users to login
- Tokens include user ID, email, and role claims

**Security Features:**
- Passwords never stored in plain text
- JWT tokens expire after configurable duration (e.g., 24 hours)
- 401 Unauthorized returned for invalid/missing tokens
- 403 Forbidden returned for insufficient permissions

**Demo:**
1. Register a new user
2. Log in and receive token
3. Try accessing admin endpoint as CUSTOMER (denied)
4. Try accessing admin endpoint as ADMIN (allowed)
5. Log out and verify token cleared

---

#### **US3 – Shopping Cart Functionality**
**Expected Outcome:**
- Cart persists in database (one cart per user)
- Users can add products to cart with quantity
- Cart displays all items with product details, quantities, subtotals
- Users can update item quantities
- Users can remove items from cart
- Cart shows grand total calculation
- Validation prevents adding out-of-stock items
- Validation prevents quantities exceeding available stock
- Cart icon in Navbar shows real-time item count

**Database Schema:**
```sql
cart (cart_id, user_id, created_at)
cart_items (item_id, cart_id, product_id, quantity, added_at)
```

**API Endpoints:**
- POST /api/cart/add - Add product to cart
- PUT /api/cart/items/{itemId} - Update quantity
- DELETE /api/cart/items/{itemId} - Remove item
- GET /api/cart - Get current user's cart
- DELETE /api/cart/clear - Clear entire cart

**Demo:**
1. Add "Sapiens" book to cart (quantity: 2)
2. Add "Clean Code" book to cart (quantity: 1)
3. View cart page showing both items with prices and total
4. Update "Sapiens" quantity to 3
5. Remove "Clean Code" from cart
6. Verify cart icon shows correct count

---

#### **US9 – Logging and Monitoring**
**Expected Outcome:**
- Spring Boot Actuator endpoints active:
  - /actuator/health - Application health status
  - /actuator/metrics - JVM and application metrics
- Structured logging configured with Logback
- Logs written to:
  - Console (for development)
  - File: logs/bookshop.log (with daily rolling)
- Log levels configured:
  - ERROR: Exceptions and critical errors
  - WARN: Validation failures, business rule violations
  - INFO: API requests, business operations
  - DEBUG: Detailed troubleshooting (optional)
- Log format: `timestamp [level] [thread] class - message`

**Logged Events:**
- All API requests (endpoint, method, parameters)
- Authentication attempts (success/failure)
- Cart operations (item added, quantity updated)
- Inventory changes
- Exceptions with stack traces

**Demo:**
1. Make API requests and verify console logs show them
2. Trigger an error and verify ERROR log entry created
3. Check logs/bookshop.log file exists with entries
4. Access /actuator/health and see UP status
5. Access /actuator/metrics and see available metric names

---

### Success Metrics
- ✅ 100% of user stories' acceptance criteria met
- ✅ All new features tested (unit + integration tests)
- ✅ CI/CD pipeline passes with new code
- ✅ No critical security vulnerabilities
- ✅ Application logging provides useful debugging information

---

## Sprint 2 Retrospective (To Be Conducted at Sprint End)

### Objective
Reflect on Sprint 2 execution, celebrate successes, and identify improvements for Sprint 3.

### What Went Well (Expected)
- ✅ Authentication provides necessary security layer
- ✅ Shopping cart enables e-commerce transactions
- ✅ JWT-based auth is stateless and scalable
- ✅ Logging improves debugging and monitoring capabilities
- ✅ Test coverage maintained with new features

### Potential Challenges / Areas to Monitor
- ⚠️ JWT token refresh not implemented (users logged out after expiry)
- ⚠️ Password reset functionality not available
- ⚠️ Cart performance may degrade with many items (optimization needed later)
- ⚠️ Frontend auth state management may need refinement
- ⚠️ Log volume may grow quickly - monitor disk space

### Lessons Learned
- Spring Security configuration requires careful CORS handling
- JWT tokens need proper validation on every request
- Cart operations require transaction management for data consistency
- Logging should be planned early, not retrofitted

### Action Items for Sprint 3
- [ ] Consider implementing JWT refresh token mechanism
- [ ] Add password reset functionality (future enhancement)
- [ ] Monitor log file sizes and configure rotation policies
- [ ] Consider Redis for cart caching (if performance issues arise)
- [ ] Document authentication flow for future developers

### Team Morale & Readiness
**Expected Morale:** Very High - significant features delivered (auth + cart)  
**Confidence:** Strong - security and core e-commerce functionality working  
**Sprint 3 Readiness:** Ready to build on cart with order management

---

## Plan Sprint 3

### Sprint 3 Goal
**Deliver end-to-end order management functionality, implement inventory alerts, and enhance the platform with GraphQL API support to enable flexible data querying.**

### Sprint 3 Duration
**1 week (Simulated)**

### Sprint 3 Backlog (Selected User Stories)
The following user stories are selected for Sprint 3:

1. **US4 - Implement Order Management System** (8 points)
2. **US5 - Low Stock Alert System** (3 points)
3. **US6 - Inventory Dashboard with Filters** (5 points)

**Total Story Points:** 16

### Sprint 3 Tasks Breakdown

#### US4: Order Management System
- [ ] Create Order and OrderItem models
- [ ] Create OrderDao and OrderService
- [ ] Implement order creation endpoint (POST `/api/orders`)
- [ ] Integrate order placement with cart (clear cart after order)
- [ ] Implement inventory reduction on order placement
- [ ] Create order history endpoint (GET `/api/orders/user/{userId}`)
- [ ] Create admin endpoint to view all orders (GET `/api/orders`)
- [ ] Create order status update endpoint (PUT `/api/orders/{orderId}/status`)
- [ ] Build frontend order confirmation page
- [ ] Build frontend order history page
- [ ] Write comprehensive tests for order functionality

#### US5: Low Stock Alert System
- [ ] Add `low_stock_threshold` field to Inventory table
- [ ] Update InventoryService to check threshold
- [ ] Create endpoint to set threshold (PUT `/api/inventory/{id}/threshold`)
- [ ] Create endpoint to get low-stock items (GET `/api/inventory/low-stock`)
- [ ] Display "Low Stock" badge on product cards
- [ ] Create admin notification UI for low-stock items
- [ ] Write tests for low-stock logic

#### US6: Inventory Dashboard with Filters
- [ ] Create dashboard backend endpoint (GET `/api/inventory/dashboard`)
- [ ] Aggregate inventory by stock status (in-stock, low-stock, out-of-stock)
- [ ] Support category filtering in dashboard endpoint
- [ ] Build dashboard UI with color-coded indicators (green/yellow/red)
- [ ] Add category filter dropdown in dashboard
- [ ] Implement real-time updates (poll or WebSocket)
- [ ] Write tests for dashboard functionality

### Expected Outcomes
By the end of Sprint 3:
- Customers can place orders and view order history
- Inventory automatically updates after orders
- Admins receive alerts for low-stock items
- Inventory dashboard provides at-a-glance status overview
- Platform supports end-to-end purchasing workflow

---

## Future Sprints (Backlog)

### Sprint 4 (Proposed)
**Focus:** Payment integration, advanced features, and performance optimization

**User Stories:**
- **US10** - Implement GraphQL API (5 points)
- **US12** - Product Image Management (5 points)
- Payment gateway integration (Stripe/PayPal) (8 points)

---

### Sprint 5 (Proposed)
**Focus:** Analytics, reporting, and customer experience enhancements

**User Stories:**
- Sales analytics dashboard (8 points)
- Product reviews and ratings (5 points)
- Email notifications for orders (3 points)

---

## Conclusion

This sprint plan provides a structured roadmap for developing the bookshop e-commerce platform using Agile methodologies and DevOps practices. By delivering incremental value through iterative sprints, the project ensures continuous improvement, high code quality, and stakeholder satisfaction.

**Key Achievements Roadmap:**
- **Sprint 1:** Foundation (Search, Tests, CI/CD, Error Handling)
- **Sprint 2:** Security & Shopping (Authentication, Cart, Monitoring)
- **Sprint 3:** Order Processing (Orders, Inventory Alerts, Dashboard)
- **Future Sprints:** Payments, Analytics, Advanced Features

**Success Metrics:**
- ✅ 85%+ test coverage
- ✅ Automated CI/CD pipeline
- ✅ Role-based security
- ✅ End-to-end customer purchase flow
- ✅ Real-time inventory management

---

**Document Version:** 1.0  
**Last Updated:** February 16, 2026  
**Author:** Emmanuel Mensah Peprah
