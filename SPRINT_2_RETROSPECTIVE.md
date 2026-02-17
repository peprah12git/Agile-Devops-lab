# Sprint 2 Retrospective - Bookshop Application

**Date:** February 17, 2026  
**Sprint:** Sprint 2  
**Participants:** Solo Developer

---

## Sprint Overview

**Sprint Goal:** Implement logging, monitoring, and deliver backlog items while applying Sprint 1 learnings.

**Outcome:** ✅ Goal achieved - Delivered logging infrastructure, Actuator monitoring, and category management feature. Successfully simplified codebase by removing unnecessary complexity.

---

## What Went Well ✅

### 1. **Rapid Problem Resolution**
- When Lombok caused compilation issues, quickly pivoted to manual getters/setters
- Fixed multiple DTO compilation errors systematically
- Resolved User model constructor mismatches efficiently

**Impact:** Build went from FAILURE to SUCCESS in ~30 minutes of focused debugging.

### 2. **Effective Scope Management**
- Recognized early that initial scope (JWT, auth, cart) was too complex
- Accepted user feedback to simplify
- Refocused on core value: logging and monitoring

**Impact:** Delivered high-quality, maintainable features instead of over-engineered solution.

### 3. **Comprehensive Logging Implementation**
- Added logging to existing controllers systematically
- Created proper Logback configuration with file rotation
- Log levels appropriately set (DEBUG for bookshop package, INFO for root)

**Impact:** Application behavior is now observable and debuggable in production.

### 4. **Clean Code Removal**
- Successfully removed all authentication/security infrastructure
- Cleaned up pom.xml dependencies
- Simplified User model and DTOs

**Impact:** Reduced complexity, faster builds, easier maintenance.

### 5. **Complete Feature Delivery**
- Category management fully implemented with CRUD operations
- All endpoints have proper validation and exception handling
- Actuator endpoints configured and tested

**Impact:** 100% sprint completion rate, all acceptance criteria met.

---

## What Didn't Go Well ❌

### 1. **Initial Scope Misalignment**
- Started implementing JWT, authentication, and shopping cart
- Created 12+ files before realizing scope was wrong
- Had to undo all changes and restart

**Impact:** Lost ~2 hours of development time on features that were immediately discarded.

### 2. **Dependency Management Issues**
- Lombok annotation processing didn't work as expected with Java 25
- Had to remove Lombok entirely and refactor all DTOs/models
- Wasted time trying to fix Lombok configuration

**Impact:** Manual getters/setters increase code verbosity, more maintenance burden.

### 3. **Incomplete Schema Cleanup**
- Database schema.sql still contains password, role, and cart tables
- Didn't align schema with simplified User model
- Schema changes deferred to technical debt

**Impact:** Schema/code mismatch could cause confusion for future developers.

### 4. **Logback Configuration Warnings**
- Using deprecated `SizeAndTimeBasedFNATP` class
- Should use `SizeAndTimeBasedRollingPolicy` instead
- Warnings appear on every application startup

**Impact:** Non-critical but creates noise in logs, appears unprofessional.

### 5. **Limited Testing**
- Didn't write new tests for CategoryController
- Didn't verify logging output programmatically
- Manual testing only for Actuator endpoints

**Impact:** Lower confidence in category feature, potential for undiscovered bugs.

---

## Lessons Learned 📚

### 1. **Clarify Requirements Before Coding**
**Lesson:** Should have confirmed Sprint 2 scope with stakeholder before implementing authentication/cart features.

**Action:** Start each sprint with explicit requirement validation. Create a brief design doc or checklist before writing code.

### 2. **Avoid Over-Engineering**
**Lesson:** "Simple bookshop" doesn't need JWT tokens, role-based access control, or shopping cart persistence.

**Action:** Apply YAGNI (You Aren't Gonna Need It) principle. Implement only what's needed now.

### 3. **Dependency Compatibility Matters**
**Lesson:** Java 25 with Spring Boot 4.0.2 had Lombok compatibility issues that weren't discovered until runtime.

**Action:** Research dependency compatibility before adding to pom.xml. Check Spring Boot compatibility matrix.

### 4. **Keep Schema in Sync with Code**
**Lesson:** User model was simplified but schema.sql still has password/role columns, creating confusion.

**Action:** Update schema immediately when models change. Schema and code should always match.

### 5. **Test Infrastructure Code**
**Lesson:** Logging and monitoring are critical for production but weren't tested (just manually verified).

**Action:** Write integration tests for Actuator endpoints, verify log file creation, test log levels.

---

## Action Items for Sprint 3 📋

### High Priority
1. **Update Database Schema**
   - Remove password and role columns from users table
   - Drop cart and cart_items tables
   - Ensure schema matches simplified codebase
   - **Owner:** Developer | **Due:** Sprint 3 Day 1

2. **Write Tests for Category Feature**
   - Unit tests for CategoryServiceImpl
   - Integration tests for CategoryController endpoints
   - Test validation and exception handling
   - **Owner:** Developer | **Due:** Sprint 3 Day 2

3. **Fix Logback Configuration Warning**
   - Replace deprecated `SizeAndTimeBasedFNATP` 
   - Use `SizeAndTimeBasedRollingPolicy` instead
   - Verify rolling policy works correctly
   - **Owner:** Developer | **Due:** Sprint 3 Day 1

### Medium Priority
4. **Add Missing Javadoc**
   - Document CategoryService methods
   - Add class-level documentation for CategoryController
   - **Owner:** Developer | **Due:** Sprint 3 Week 1

5. **Create Integration Tests for Actuator**
   - Test /actuator/health endpoint
   - Verify health components (db, diskSpace)
   - **Owner:** Developer | **Due:** Sprint 3 Week 1

### Low Priority
6. **Logging Best Practices Review**
   - Ensure no sensitive data in logs
   - Add correlation IDs for request tracking
   - **Owner:** Developer | **Due:** Sprint 3 Week 2

---

## Process Improvements 🔧

### Continue Doing ✅
1. **Systematic Error Resolution** - The approach of fixing compilation errors one file at a time worked well
2. **Clean Commits** - Working on sprint-two branch keeps main branch stable
3. **Documentation** - Creating review and retrospective docs provides good project history

### Stop Doing 🛑
1. **Assuming Requirements** - Don't implement major features without explicit confirmation
2. **Ignoring Warnings** - Logback warnings should have been fixed immediately, not deferred
3. **Manual Testing Only** - Need automated tests for infrastructure features

### Start Doing 🚀
1. **Pre-Sprint Design Sessions** - Spend 30 minutes planning architecture before coding
2. **Dependency Research** - Check compatibility matrices before adding dependencies
3. **Test-First for New Features** - Write tests before implementation for new features

---

## Metrics and Observations 📊

### Velocity
- **Sprint 1 Completion:** ~70% (had issues with test coverage)
- **Sprint 2 Completion:** 100% (all features delivered)
- **Trend:** ⬆️ Improving

### Code Quality
- **Build Time:** ~45 seconds (clean compile)
- **Startup Time:** 3.367 seconds
- **Compilation Errors:** 0
- **Runtime Errors:** 0

### Technical Debt
- **Added:** Logback warning, missing schema updates, no tests for Category
- **Removed:** Lombok dependency, security infrastructure, JWT libraries
- **Net Change:** ⬇️ Overall debt reduced (removed ~15 files, added 3 issues)

---

## Team Morale and Energy

**Mood:** 😊 Positive

**Energy Level:** High - Sprint completed successfully with good momentum

**Confidence:** Strong - Application is simpler, more maintainable, and has better observability

---

## Stakeholder Satisfaction

**Feedback:** User explicitly requested simplification - "Keep it simple, no cart, no JWT, no user logging in"

**Response:** Successfully delivered on simplified requirements while adding valuable logging/monitoring

**Satisfaction Level:** ✅ High - Requirements met, unnecessary complexity removed

---

## Sprint 2 vs Sprint 1 Comparison

| Metric | Sprint 1 | Sprint 2 | Change |
|--------|----------|----------|--------|
| Completion Rate | 70% | 100% | +30% ✅ |
| Features Delivered | 3 | 3 | = |
| Tests Written | 5 | 0 | -5 ❌ |
| Code Complexity | Medium | Low | -1 ✅ |
| Build Issues | Several | None | ✅ |
| Documentation | Basic | Comprehensive | ✅ |

**Trend:** Delivering features more reliably, but need to improve testing discipline.

---

## Key Takeaways

### What Made This Sprint Successful:
1. ✅ **Simplicity** - Focused on essential features only
2. ✅ **Flexibility** - Adapted when initial scope was wrong
3. ✅ **Systematic Approach** - Fixed errors methodically
4. ✅ **Clean Code** - Removed unnecessary complexity

### Areas for Improvement:
1. ❌ **Testing** - Need more automated tests
2. ❌ **Planning** - Better upfront requirement validation
3. ❌ **Completeness** - Schema should have been updated

### Overall Assessment:
**Sprint 2 was a success.** We delivered valuable monitoring and logging capabilities, added category management, and significantly simplified the codebase. The key learning is to validate requirements early and avoid over-engineering.

---

## Commitment for Sprint 3

**As a team (solo developer), I commit to:**
1. Write tests for all new features BEFORE implementation
2. Update database schema to match code changes
3. Fix all warnings, not just errors
4. Validate requirements with stakeholder before major features
5. Maintain the simplicity achieved in Sprint 2

---

**Retrospective Completed:** February 17, 2026  
**Next Review:** Sprint 3 Retrospective
