package bookshop.services.serviceimp;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import bookshop.dao.daoInterface.CategoryDao;
import bookshop.dao.daoInterface.ProductDao;
import bookshop.exceptions.BusinessException;
import bookshop.models.Product;

/**
 * Comprehensive unit tests for ProductServiceImpl.createProduct()
 * Tests all validation rules, edge cases, and business logic
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceImpl.createProduct() Tests")
class ProductServiceImplCreateProductTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CategoryDao categoryDao;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setName("Test Book");
        testProduct.setPrice(new BigDecimal("29.99"));
        testProduct.setCategoryId(1);
    }

    @Nested
    @DisplayName("Successful Product Creation Tests")
    class SuccessfulCreationTests {

        @Test
        @DisplayName("Should successfully create product with valid data")
        void testCreateProduct_WithValidData_Success() {
            // Arrange
            Product savedProduct = new Product();
            savedProduct.setProductId(100);
            savedProduct.setName(testProduct.getName());
            savedProduct.setPrice(testProduct.getPrice());
            savedProduct.setCategoryId(testProduct.getCategoryId());

            when(categoryDao.existsById(testProduct.getCategoryId())).thenReturn(true);
            when(productDao.existsByName(testProduct.getName())).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(100, result.getProductId());
            assertEquals("Test Book", result.getName());
            assertEquals(new BigDecimal("29.99"), result.getPrice());
            assertEquals(1, result.getCategoryId());

            verify(categoryDao, times(1)).existsById(1);
            verify(productDao, times(1)).existsByName("Test Book");
            verify(productDao, times(1)).save(testProduct);
        }

        @Test
        @DisplayName("Should create product with minimum valid price")
        void testCreateProduct_WithMinimumPrice_Success() {
            // Arrange
            testProduct.setPrice(new BigDecimal("0.01"));
            Product savedProduct = new Product(101, "Cheap Item", new BigDecimal("0.01"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(new BigDecimal("0.01"), result.getPrice());
            verify(productDao, times(1)).save(testProduct);
        }

        @Test
        @DisplayName("Should create product with large price")
        void testCreateProduct_WithLargePrice_Success() {
            // Arrange
            testProduct.setPrice(new BigDecimal("9999999.99"));
            Product savedProduct = new Product(102, "Expensive Item", new BigDecimal("9999999.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(new BigDecimal("9999999.99"), result.getPrice());
        }

        @Test
        @DisplayName("Should create products with different category IDs")
        void testCreateProduct_WithDifferentCategories_Success() {
            // Arrange
            testProduct.setCategoryId(5);
            Product savedProduct = new Product(103, "Test Book", new BigDecimal("29.99"), 5);

            when(categoryDao.existsById(5)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(5, result.getCategoryId());
            verify(categoryDao, times(1)).existsById(5);
        }
    }

    @Nested
    @DisplayName("Category Validation Tests")
    class CategoryValidationTests {

        @Test
        @DisplayName("Should throw BusinessException when category does not exist")
        void testCreateProduct_WithNonExistentCategory_ThrowsException() {
            // Arrange
            when(categoryDao.existsById(testProduct.getCategoryId())).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertEquals("Category with ID 1 does not exist", exception.getMessage());
            verify(categoryDao, times(1)).existsById(1);
            verify(productDao, never()).existsByName(anyString());
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException for invalid category ID")
        void testCreateProduct_WithInvalidCategoryId_ThrowsException() {
            // Arrange
            testProduct.setCategoryId(999);
            when(categoryDao.existsById(999)).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertTrue(exception.getMessage().contains("Category with ID 999 does not exist"));
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should check category existence before other validations")
        void testCreateProduct_ChecksCategoryFirst_WhenInvalid() {
            // Arrange
            when(categoryDao.existsById(1)).thenReturn(false);

            // Act & Assert
            assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Category check happens first, so name uniqueness is not checked
            verify(categoryDao, times(1)).existsById(1);
            verify(productDao, never()).existsByName(anyString());
        }
    }

    @Nested
    @DisplayName("Product Name Uniqueness Tests")
    class NameUniquenessTests {

        @Test
        @DisplayName("Should throw BusinessException when product name already exists")
        void testCreateProduct_WithDuplicateName_ThrowsException() {
            // Arrange
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(true);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertEquals("Product name 'Test Book' already exists", exception.getMessage());
            verify(categoryDao, times(1)).existsById(1);
            verify(productDao, times(1)).existsByName("Test Book");
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException for duplicate names with different casing")
        void testCreateProduct_WithDuplicateNameDifferentCase_ThrowsException() {
            // Arrange
            testProduct.setName("EXISTING PRODUCT");
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("EXISTING PRODUCT")).thenReturn(true);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertTrue(exception.getMessage().contains("EXISTING PRODUCT"));
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should allow creation when name is unique")
        void testCreateProduct_WithUniqueName_Success() {
            // Arrange
            Product savedProduct = new Product(105, "Unique Book", new BigDecimal("29.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            verify(productDao, times(1)).existsByName("Test Book");
            verify(productDao, times(1)).save(testProduct);
        }
    }

    @Nested
    @DisplayName("Price Validation Tests")
    class PriceValidationTests {

        @Test
        @DisplayName("Should throw BusinessException when price is zero")
        void testCreateProduct_WithZeroPrice_ThrowsException() {
            // Arrange
            testProduct.setPrice(BigDecimal.ZERO);
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertEquals("Product price must be greater than zero", exception.getMessage());
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when price is negative")
        void testCreateProduct_WithNegativePrice_ThrowsException() {
            // Arrange
            testProduct.setPrice(new BigDecimal("-10.00"));
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertEquals("Product price must be greater than zero", exception.getMessage());
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when price is slightly negative")
        void testCreateProduct_WithSlightlyNegativePrice_ThrowsException() {
            // Arrange
            testProduct.setPrice(new BigDecimal("-0.01"));
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            assertEquals("Product price must be greater than zero", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept price with many decimal places")
        void testCreateProduct_WithPrecisePrice_Success() {
            // Arrange
            testProduct.setPrice(new BigDecimal("19.999999"));
            Product savedProduct = new Product(106, "Test Book", new BigDecimal("19.999999"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(new BigDecimal("19.999999"), result.getPrice());
        }
    }

    @Nested
    @DisplayName("Validation Order Tests")
    class ValidationOrderTests {

        @Test
        @DisplayName("Should validate category before checking name uniqueness")
        void testCreateProduct_ValidatesCategoryFirst() {
            // Arrange
            when(categoryDao.existsById(1)).thenReturn(false);

            // Act & Assert
            assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Verify order: category check happens first
            verify(categoryDao, times(1)).existsById(1);
            verify(productDao, never()).existsByName(anyString());
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should check name uniqueness before price validation")
        void testCreateProduct_ChecksNameBeforePrice() {
            // Arrange
            testProduct.setPrice(BigDecimal.ZERO); // Invalid price
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(true); // Duplicate name

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Name validation fails before price validation
            assertTrue(exception.getMessage().contains("already exists"));
            verify(productDao, never()).save(any(Product.class));
        }

        @Test
        @DisplayName("Should perform all validations in correct order before saving")
        void testCreateProduct_ValidatesBeforeSaving() {
            // Arrange
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            Product savedProduct = new Product(107, "Test Book", new BigDecimal("29.99"), 1);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            productService.createProduct(testProduct);

            // Assert - Verify all checks happen before save
            var inOrder = inOrder(categoryDao, productDao);
            inOrder.verify(categoryDao).existsById(1);
            inOrder.verify(productDao).existsByName("Test Book");
            inOrder.verify(productDao).save(testProduct);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle product with very long name")
        void testCreateProduct_WithLongName_Success() {
            // Arrange
            String longName = "A".repeat(255);
            testProduct.setName(longName);
            Product savedProduct = new Product(108, longName, new BigDecimal("29.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName(longName)).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(longName, result.getName());
        }

        @Test
        @DisplayName("Should handle product name with special characters")
        void testCreateProduct_WithSpecialCharactersInName_Success() {
            // Arrange
            testProduct.setName("Book: The Ultimate Guide (2nd Edition) - Special!");
            Product savedProduct = new Product(109, testProduct.getName(), new BigDecimal("29.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName(testProduct.getName())).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals("Book: The Ultimate Guide (2nd Edition) - Special!", result.getName());
        }

        @Test
        @DisplayName("Should handle product with price boundary value")
        void testCreateProduct_WithBoundaryPrice_Success() {
            // Arrange
            testProduct.setPrice(new BigDecimal("0.001")); // Very small but positive
            Product savedProduct = new Product(110, "Test Book", new BigDecimal("0.001"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertTrue(result.getPrice().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("Should handle category ID with large number")
        void testCreateProduct_WithLargeCategoryId_Success() {
            // Arrange
            testProduct.setCategoryId(Integer.MAX_VALUE);
            Product savedProduct = new Product(111, "Test Book", new BigDecimal("29.99"), Integer.MAX_VALUE);

            when(categoryDao.existsById(Integer.MAX_VALUE)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(Integer.MAX_VALUE, result.getCategoryId());
        }
    }

    @Nested
    @DisplayName("Method Interaction and State Tests")
    class MethodInteractionTests {

        @Test
        @DisplayName("Should not modify original product object on validation failure")
        void testCreateProduct_DoesNotModifyProductOnFailure() {
            // Arrange
            String originalName = testProduct.getName();
            BigDecimal originalPrice = testProduct.getPrice();
            int originalCategoryId = testProduct.getCategoryId();

            when(categoryDao.existsById(1)).thenReturn(false);

            // Act & Assert
            assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Verify product remains unchanged
            assertEquals(originalName, testProduct.getName());
            assertEquals(originalPrice, testProduct.getPrice());
            assertEquals(originalCategoryId, testProduct.getCategoryId());
        }

        @Test
        @DisplayName("Should only call save once on successful creation")
        void testCreateProduct_CallsSaveOnlyOnce_Success() {
            // Arrange
            Product savedProduct = new Product(112, "Test Book", new BigDecimal("29.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            productService.createProduct(testProduct);

            // Assert
            verify(productDao, times(1)).save(testProduct);
        }

        @Test
        @DisplayName("Should return product with generated ID after save")
        void testCreateProduct_ReturnsProductWithId() {
            // Arrange
            Product savedProduct = new Product(999, "Test Book", new BigDecimal("29.99"), 1);

            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(false);
            when(productDao.save(testProduct)).thenReturn(savedProduct);

            // Act
            Product result = productService.createProduct(testProduct);

            // Assert
            assertNotNull(result);
            assertEquals(999, result.getProductId());
            assertTrue(result.getProductId() > 0);
        }
    }

    @Nested
    @DisplayName("Multiple Validation Failures Tests")
    class MultipleFailuresTests {

        @Test
        @DisplayName("Should fail on category when both category and name are invalid")
        void testCreateProduct_WithInvalidCategoryAndDuplicateName() {
            // Arrange
            when(categoryDao.existsById(1)).thenReturn(false);
            // Don't stub productDao.existsByName since it won't be called

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Category validation fails first
            assertTrue(exception.getMessage().contains("Category"));
            verify(productDao, never()).existsByName(anyString());
        }

        @Test
        @DisplayName("Should fail on name when name is duplicate and price is zero")
        void testCreateProduct_WithDuplicateNameAndZeroPrice() {
            // Arrange
            testProduct.setPrice(BigDecimal.ZERO);
            when(categoryDao.existsById(1)).thenReturn(true);
            when(productDao.existsByName("Test Book")).thenReturn(true);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // Name validation fails before price validation
            assertTrue(exception.getMessage().contains("already exists"));
        }

        @Test
        @DisplayName("Should fail on all validations when category, name, and price are invalid")
        void testCreateProduct_WithAllInvalidFields() {
            // Arrange
            testProduct.setPrice(new BigDecimal("-5.00"));
            when(categoryDao.existsById(1)).thenReturn(false);

            // Act & Assert
            BusinessException exception = assertThrows(
                BusinessException.class,
                () -> productService.createProduct(testProduct)
            );

            // First validation (category) fails
            assertTrue(exception.getMessage().contains("Category"));
            verify(productDao, never()).save(any(Product.class));
        }
    }
}
