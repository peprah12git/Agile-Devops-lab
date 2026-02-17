Sprint 2 Retrospective – Bookshop Application

Date: February 17, 2026
Sprint Duration: Sprint 2
Developer: Solo Developer
Sprint Goal: Deliver logging and monitoring capabilities and implement the category management feature while applying process improvements identified in Sprint 1.

1. Sprint Overview

Sprint 2 focused on strengthening the internal quality and observability of the Bookshop application while delivering a functional module for category management.

During sprint planning, requirements were clarified, and the scope was simplified based on stakeholder feedback. Features such as authentication and shopping cart functionality were intentionally excluded to maintain a lightweight and focused system.

Overall Outcome:
✅ Sprint goal achieved
✅ All planned features delivered
⚠ Testing discipline requires reinforcement

2. What Went Well ✅
2.1 Clear Scope Alignment and Simplification

Stakeholder clarification ensured unnecessary complexity (authentication, cart) was removed.

Scope validation improved decision-making and reduced wasted development time.
Impact: Leaner architecture, reduced technical debt, faster builds, and improved maintainability.

2.2 Improved Observability

Structured logging implemented using Logback with file rotation and proper log levels.

Spring Boot Actuator endpoints added for health monitoring.
Impact: Easier debugging, production observability, and better insight into application behavior.

2.3 Clean Code and Dependency Management

Removed Lombok (due to Java 25 incompatibility), unused dependencies, and obsolete security infrastructure.
Impact: Stable builds, zero compilation errors, and easier maintenance.

2.4 Complete Feature Delivery

Category Management module fully implemented: CRUD operations, validation, exception handling, and consistent REST design.
Impact: All planned backlog items completed within the sprint.

3. What Didn’t Go Well 
3.1 Testing Discipline

No automated tests were written for the Category feature.

Logging and Actuator endpoints were manually verified only.

Coverage measurement tools (e.g., JaCoCo) were not integrated.
Impact: Lower confidence in new features and risk of undetected bugs.

3.2 Time Lost Due to Initial Scope Misinterpretation

Initial partial implementation of authentication and shopping cart features was later discarded.
Impact: Approximately two hours of development time lost.

3.3 Minor Technical Debt

Database schema not fully aligned with simplified models.

Logback configuration generated non-critical warnings.

Infrastructure changes were not covered by integration tests.
Impact: Minor risk of confusion and additional cleanup work.

4. Key Lessons Learned 📚

Validate sprint scope before starting implementation.

Simplification improves delivery speed and maintainability.

Observability is critical for production readiness.

Identifying improvements is not enough — disciplined execution is essential.

Testing discipline must be enforced, not assumed.

5. Metrics and Observations 📊
Metric	Value / Observation
Sprint Goal Completion	100%
Feature Delivery	Category Management implemented fully
Build Time	~45 seconds
Startup Time	3.367 seconds
Compilation Errors	0
Runtime Errors	0
Technical Debt	Reduced (removed unused security and dependencies)
Testing Coverage	Not implemented

Trend: Feature delivery is reliable; codebase is simpler and more maintainable, but testing discipline remains an area for improvement.

6. Overall Assessment

Sprint 2 was successful in delivering the intended features and improving system observability and maintainability. The sprint demonstrated clear scope control, effective simplification, and good documentation practices.
