package bookshop.services.serviceimp;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import bookshop.dao.daoInterface.UserDao;
import bookshop.models.User;

/**
 * Comprehensive unit tests for UserServiceImpl.registerUser()
 * Tests all validation rules, edge cases, and business logic
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl.registerUser() Tests")
class UserServiceImplRegisterUserTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setCourse("Computer Science");
        testUser.setAge(25);
    }

    @Nested
    @DisplayName("Successful User Registration Tests")
    class SuccessfulRegistrationTests {

        @Test
        @DisplayName("Should successfully register user with valid data")
        void testRegisterUser_WithValidData_Success() {
            // Arrange
            User savedUser = new User();
            savedUser.setId(1);
            savedUser.setName(testUser.getName());
            savedUser.setEmail(testUser.getEmail());
            savedUser.setCourse(testUser.getCourse());
            savedUser.setAge(testUser.getAge());

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("John Doe", result.getName());
            assertEquals("john.doe@example.com", result.getEmail());
            assertEquals("Computer Science", result.getCourse());
            assertEquals(25, result.getAge());

            verify(userDao, times(1)).findByEmail("john.doe@example.com");
            verify(userDao, times(1)).save(testUser);
        }

        @Test
        @DisplayName("Should successfully register user without age")
        void testRegisterUser_WithoutAge_Success() {
            // Arrange
            testUser.setAge(null);
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", null);
            savedUser.setId(2);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertNull(result.getAge());
            verify(userDao, times(1)).save(testUser);
        }

        @Test
        @DisplayName("Should successfully register user without course")
        void testRegisterUser_WithoutCourse_Success() {
            // Arrange
            testUser.setCourse(null);
            User savedUser = new User();
            savedUser.setId(3);
            savedUser.setName("John Doe");
            savedUser.setEmail("john.doe@example.com");
            savedUser.setAge(25);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertNull(result.getCourse());
            verify(userDao, times(1)).save(testUser);
        }

        @Test
        @DisplayName("Should successfully register user with age 0")
        void testRegisterUser_WithZeroAge_Success() {
            // Arrange
            testUser.setAge(0);
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", 0);
            savedUser.setId(4);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(0, result.getAge());
        }

        @Test
        @DisplayName("Should successfully register user with very large age")
        void testRegisterUser_WithLargeAge_Success() {
            // Arrange
            testUser.setAge(120);
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", 120);
            savedUser.setId(5);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(120, result.getAge());
        }
    }

    @Nested
    @DisplayName("User Null Validation Tests")
    class UserNullValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when user is null")
        void testRegisterUser_WithNullUser_ThrowsException() {
            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(null)
            );

            assertEquals("User cannot be null", exception.getMessage());
            verify(userDao, never()).findByEmail(anyString());
            verify(userDao, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Name Validation Tests")
    class NameValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void testRegisterUser_WithNullName_ThrowsException() {
            // Arrange
            testUser.setName(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("User name cannot be null or empty", exception.getMessage());
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is empty")
        void testRegisterUser_WithEmptyName_ThrowsException() {
            // Arrange
            testUser.setName("");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("User name cannot be null or empty", exception.getMessage());
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when name is whitespace only")
        void testRegisterUser_WithWhitespaceName_ThrowsException() {
            // Arrange
            testUser.setName("   ");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("User name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept name with single character")
        void testRegisterUser_WithSingleCharacterName_Success() {
            // Arrange
            testUser.setName("J");
            User savedUser = new User("J", "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(6);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals("J", result.getName());
        }
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when email is null")
        void testRegisterUser_WithNullEmail_ThrowsException() {
            // Arrange
            testUser.setEmail(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Email cannot be null or empty", exception.getMessage());
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when email is empty")
        void testRegisterUser_WithEmptyEmail_ThrowsException() {
            // Arrange
            testUser.setEmail("");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Email cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when email is whitespace only")
        void testRegisterUser_WithWhitespaceEmail_ThrowsException() {
            // Arrange
            testUser.setEmail("   ");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Email cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid email format without @")
        void testRegisterUser_WithInvalidEmailNoAt_ThrowsException() {
            // Arrange
            testUser.setEmail("invalidemail.com");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Invalid email format", exception.getMessage());
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid email format without domain")
        void testRegisterUser_WithInvalidEmailNoDomain_ThrowsException() {
            // Arrange
            testUser.setEmail("user@");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for email with spaces")
        void testRegisterUser_WithEmailContainingSpaces_ThrowsException() {
            // Arrange
            testUser.setEmail("user name@example.com");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Invalid email format", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept valid email with plus sign")
        void testRegisterUser_WithEmailContainingPlus_Success() {
            // Arrange
            testUser.setEmail("user+test@example.com");
            User savedUser = new User("John Doe", "user+test@example.com", "Computer Science", 25);
            savedUser.setId(7);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals("user+test@example.com", result.getEmail());
        }

        @Test
        @DisplayName("Should accept valid email with dots and underscores")
        void testRegisterUser_WithEmailContainingDotsAndUnderscores_Success() {
            // Arrange
            testUser.setEmail("first.last_name@example.co.uk");
            User savedUser = new User("John Doe", "first.last_name@example.co.uk", "Computer Science", 25);
            savedUser.setId(8);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals("first.last_name@example.co.uk", result.getEmail());
        }

        @Test
        @DisplayName("Should accept valid email with numbers")
        void testRegisterUser_WithEmailContainingNumbers_Success() {
            // Arrange
            testUser.setEmail("user123@example456.com");
            User savedUser = new User("John Doe", "user123@example456.com", "Computer Science", 25);
            savedUser.setId(9);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals("user123@example456.com", result.getEmail());
        }
    }

    @Nested
    @DisplayName("Email Uniqueness Tests")
    class EmailUniquenessTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when email already exists")
        void testRegisterUser_WithExistingEmail_ThrowsException() {
            // Arrange
            User existingUser = new User("Jane Smith", "john.doe@example.com", "Mathematics", 30);
            existingUser.setId(100);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.of(existingUser));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Email already exists: john.doe@example.com", exception.getMessage());
            verify(userDao, times(1)).findByEmail("john.doe@example.com");
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for case-sensitive duplicate email")
        void testRegisterUser_WithDuplicateEmailDifferentCase_ThrowsException() {
            // Arrange
            testUser.setEmail("JOHN.DOE@EXAMPLE.COM");
            User existingUser = new User("Jane Smith", "JOHN.DOE@EXAMPLE.COM", "Mathematics", 30);
            existingUser.setId(101);

            when(userDao.findByEmail("JOHN.DOE@EXAMPLE.COM")).thenReturn(Optional.of(existingUser));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertTrue(exception.getMessage().contains("Email already exists"));
            verify(userDao, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Age Validation Tests")
    class AgeValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when age is negative")
        void testRegisterUser_WithNegativeAge_ThrowsException() {
            // Arrange
            testUser.setAge(-1);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Age cannot be negative", exception.getMessage());
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when age is large negative")
        void testRegisterUser_WithLargeNegativeAge_ThrowsException() {
            // Arrange
            testUser.setAge(-100);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Age cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept null age")
        void testRegisterUser_WithNullAge_Success() {
            // Arrange
            testUser.setAge(null);
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", null);
            savedUser.setId(10);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertNull(result.getAge());
        }
    }

    @Nested
    @DisplayName("Validation Order Tests")
    class ValidationOrderTests {

        @Test
        @DisplayName("Should validate user null before checking fields")
        void testRegisterUser_ValidatesUserNullFirst() {
            // Act & Assert
            assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(null)
            );

            verify(userDao, never()).findByEmail(anyString());
        }

        @Test
        @DisplayName("Should validate name before email uniqueness check")
        void testRegisterUser_ValidatesNameBeforeEmailUniqueness() {
            // Arrange
            testUser.setName(null);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertTrue(exception.getMessage().contains("name"));
            verify(userDao, never()).findByEmail(anyString());
        }

        @Test
        @DisplayName("Should validate email before checking uniqueness")
        void testRegisterUser_ValidatesEmailFormatBeforeUniqueness() {
            // Arrange
            testUser.setEmail("invalidemail");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            assertEquals("Invalid email format", exception.getMessage());
            verify(userDao, never()).findByEmail(anyString());
        }

        @Test
        @DisplayName("Should check email uniqueness before saving")
        void testRegisterUser_ChecksEmailUniquenessBeforeSave() {
            // Arrange
            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(11);
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            userService.registerUser(testUser);

            // Assert - Verify call order
            var inOrder = inOrder(userDao);
            inOrder.verify(userDao).findByEmail("john.doe@example.com");
            inOrder.verify(userDao).save(testUser);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long names")
        void testRegisterUser_WithVeryLongName_Success() {
            // Arrange
            String longName = "A".repeat(255);
            testUser.setName(longName);
            User savedUser = new User(longName, "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(12);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(longName, result.getName());
        }

        @Test
        @DisplayName("Should handle names with special characters")
        void testRegisterUser_WithSpecialCharactersInName_Success() {
            // Arrange
            testUser.setName("John O'Brien-Smith Jr.");
            User savedUser = new User("John O'Brien-Smith Jr.", "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(13);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals("John O'Brien-Smith Jr.", result.getName());
        }

        @Test
        @DisplayName("Should handle very long course names")
        void testRegisterUser_WithLongCourse_Success() {
            // Arrange
            String longCourse = "Computer Science and Information Technology with Business Management";
            testUser.setCourse(longCourse);
            User savedUser = new User("John Doe", "john.doe@example.com", longCourse, 25);
            savedUser.setId(14);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(longCourse, result.getCourse());
        }

        @Test
        @DisplayName("Should handle maximum integer age")
        void testRegisterUser_WithMaxIntegerAge_Success() {
            // Arrange
            testUser.setAge(Integer.MAX_VALUE);
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", Integer.MAX_VALUE);
            savedUser.setId(15);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(Integer.MAX_VALUE, result.getAge());
        }
    }

    @Nested
    @DisplayName("Method Interaction and State Tests")
    class MethodInteractionTests {

        @Test
        @DisplayName("Should not modify original user object on validation failure")
        void testRegisterUser_DoesNotModifyUserOnFailure() {
            // Arrange
            String originalName = testUser.getName();
            String originalEmail = testUser.getEmail();
            Integer originalAge = testUser.getAge();

            testUser.setName(null);

            // Act & Assert
            assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            // Verify user state (email and age remain unchanged)
            assertNull(testUser.getName());
            assertEquals(originalEmail, testUser.getEmail());
            assertEquals(originalAge, testUser.getAge());
        }

        @Test
        @DisplayName("Should only call save once on successful registration")
        void testRegisterUser_CallsSaveOnlyOnce_Success() {
            // Arrange
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(16);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            userService.registerUser(testUser);

            // Assert
            verify(userDao, times(1)).save(testUser);
        }

        @Test
        @DisplayName("Should return user with generated ID after registration")
        void testRegisterUser_ReturnsUserWithId() {
            // Arrange
            User savedUser = new User("John Doe", "john.doe@example.com", "Computer Science", 25);
            savedUser.setId(999);

            when(userDao.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
            when(userDao.save(testUser)).thenReturn(savedUser);

            // Act
            User result = userService.registerUser(testUser);

            // Assert
            assertNotNull(result);
            assertEquals(999, result.getId());
            assertTrue(result.getId() > 0);
        }
    }

    @Nested
    @DisplayName("Multiple Validation Failures Tests")
    class MultipleFailuresTests {

        @Test
        @DisplayName("Should fail on name when both name and email are invalid")
        void testRegisterUser_WithInvalidNameAndEmail() {
            // Arrange
            testUser.setName(null);
            testUser.setEmail("invalidemail");

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            // Name validation fails first
            assertTrue(exception.getMessage().contains("name"));
            verify(userDao, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should fail on email format when email is invalid and age is negative")
        void testRegisterUser_WithInvalidEmailAndNegativeAge() {
            // Arrange
            testUser.setEmail("invalidemail");
            testUser.setAge(-5);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            // Email format validation fails before age validation
            assertTrue(exception.getMessage().contains("email format"));
        }

        @Test
        @DisplayName("Should fail on all validations when all fields are invalid")
        void testRegisterUser_WithAllInvalidFields() {
            // Arrange
            testUser.setName("");
            testUser.setEmail("invalid");
            testUser.setAge(-1);

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(testUser)
            );

            // First validation (name) fails
            assertTrue(exception.getMessage().contains("name"));
            verify(userDao, never()).save(any(User.class));
        }
    }
}
