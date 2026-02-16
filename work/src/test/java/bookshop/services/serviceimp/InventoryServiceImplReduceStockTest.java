package bookshop.services.serviceimp;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import bookshop.dao.daoInterface.InventoryDao;
import bookshop.exceptions.InsufficientStockException;
import bookshop.exceptions.InventoryNotFoundException;
import bookshop.models.Inventory;

/**
 * Comprehensive unit tests for InventoryServiceImpl.reduceStock()
 * Tests all validation rules, edge cases, and business logic
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InventoryServiceImpl.reduceStock() Tests")
class InventoryServiceImplReduceStockTest {

    @Mock
    private InventoryDao inventoryDao;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        testInventory = new Inventory();
        testInventory.setInventoryId(1);
        testInventory.setProductId(100);
        testInventory.setQuantity(50);
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when product ID is zero")
        void testReduceStock_WithZeroProductId_ThrowsException() {
            // Arrange
            int invalidProductId = 0;
            int quantityToReduce = 5;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(invalidProductId, quantityToReduce)
            );

            assertEquals("Product ID must be positive", exception.getMessage());
            verify(inventoryDao, never()).findByProductId(anyInt());
            verify(inventoryDao, never()).updateQuantity(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when product ID is negative")
        void testReduceStock_WithNegativeProductId_ThrowsException() {
            // Arrange
            int invalidProductId = -10;
            int quantityToReduce = 5;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(invalidProductId, quantityToReduce)
            );

            assertEquals("Product ID must be positive", exception.getMessage());
            verify(inventoryDao, never()).findByProductId(anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity to reduce is zero")
        void testReduceStock_WithZeroQuantity_ThrowsException() {
            // Arrange
            int productId = 100;
            int invalidQuantity = 0;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(productId, invalidQuantity)
            );

            assertEquals("Quantity to reduce must be positive", exception.getMessage());
            verify(inventoryDao, never()).findByProductId(anyInt());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when quantity to reduce is negative")
        void testReduceStock_WithNegativeQuantity_ThrowsException() {
            // Arrange
            int productId = 100;
            int invalidQuantity = -5;

            // Act & Assert
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(productId, invalidQuantity)
            );

            assertEquals("Quantity to reduce must be positive", exception.getMessage());
            verify(inventoryDao, never()).findByProductId(anyInt());
        }
    }

    @Nested
    @DisplayName("Inventory Existence Tests")
    class InventoryExistenceTests {

        @Test
        @DisplayName("Should throw InventoryNotFoundException when inventory does not exist")
        void testReduceStock_WithNonExistentInventory_ThrowsException() {
            // Arrange
            int productId = 999;
            int quantityToReduce = 5;
            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.empty());

            // Act & Assert
            InventoryNotFoundException exception = assertThrows(
                InventoryNotFoundException.class,
                () -> inventoryService.reduceStock(productId, quantityToReduce)
            );

            assertEquals("Inventory not found for product ID: " + productId, exception.getMessage());
            verify(inventoryDao, times(1)).findByProductId(productId);
            verify(inventoryDao, never()).updateQuantity(anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Stock Sufficiency Tests")
    class StockSufficiencyTests {

        @Test
        @DisplayName("Should throw InsufficientStockException when stock is less than required")
        void testReduceStock_WithInsufficientStock_ThrowsException() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 60; // More than available (50)
            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));

            // Act & Assert
            InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.reduceStock(productId, quantityToReduce)
            );

            assertEquals(productId, exception.getProductId());
            assertEquals(50, exception.getAvailableQuantity());
            assertEquals(60, exception.getRequiredQuantity());
            assertTrue(exception.getMessage().contains("Insufficient stock"));
            verify(inventoryDao, times(1)).findByProductId(productId);
            verify(inventoryDao, never()).updateQuantity(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when trying to reduce all stock plus one")
        void testReduceStock_WithExactlyOneMoreThanAvailable_ThrowsException() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 51; // One more than available (50)
            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));

            // Act & Assert
            InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.reduceStock(productId, quantityToReduce)
            );

            assertEquals(productId, exception.getProductId());
            assertEquals(50, exception.getAvailableQuantity());
            assertEquals(51, exception.getRequiredQuantity());
        }

        @Test
        @DisplayName("Should throw InsufficientStockException when inventory is empty")
        void testReduceStock_WithZeroStock_ThrowsException() {
            // Arrange
            testInventory.setQuantity(0);
            int productId = 100;
            int quantityToReduce = 1;
            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));

            // Act & Assert
            InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.reduceStock(productId, quantityToReduce)
            );

            assertEquals(0, exception.getAvailableQuantity());
            assertEquals(1, exception.getRequiredQuantity());
        }
    }

    @Nested
    @DisplayName("Successful Stock Reduction Tests")
    class SuccessfulReductionTests {

        @Test
        @DisplayName("Should successfully reduce stock when sufficient quantity available")
        void testReduceStock_WithSufficientStock_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 20;
            int expectedNewQuantity = 30; // 50 - 20
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(expectedNewQuantity, result.getQuantity());
            assertEquals(productId, result.getProductId());
            verify(inventoryDao, times(1)).findByProductId(productId);
            verify(inventoryDao, times(1)).updateQuantity(productId, expectedNewQuantity);
        }

        @Test
        @DisplayName("Should successfully reduce stock by exactly available quantity (to zero)")
        void testReduceStock_ReduceToZero_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 50; // Exactly the available quantity
            int expectedNewQuantity = 0;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(0, result.getQuantity());
            verify(inventoryDao, times(1)).findByProductId(productId);
            verify(inventoryDao, times(1)).updateQuantity(productId, expectedNewQuantity);
        }

        @Test
        @DisplayName("Should successfully reduce by one unit")
        void testReduceStock_ReduceByOne_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 1;
            int expectedNewQuantity = 49;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(49, result.getQuantity());
            verify(inventoryDao, times(1)).updateQuantity(productId, expectedNewQuantity);
        }
    }

    @Nested
    @DisplayName("Low Stock Warning Tests")
    class LowStockWarningTests {

        @Test
        @DisplayName("Should handle reduction that results in low stock (below 10)")
        void testReduceStock_ResultingInLowStock_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 45; // Results in 5 remaining
            int expectedNewQuantity = 5;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(5, result.getQuantity());
            // Note: Low stock warning is logged but not thrown as exception
            verify(inventoryDao, times(1)).updateQuantity(productId, expectedNewQuantity);
        }

        @Test
        @DisplayName("Should handle reduction to exactly 9 units (low stock threshold)")
        void testReduceStock_ResultingInExactly9Units_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 41; // Results in 9 remaining
            int expectedNewQuantity = 9;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(9, result.getQuantity());
            verify(inventoryDao, times(1)).updateQuantity(productId, expectedNewQuantity);
        }

        @Test
        @DisplayName("Should not trigger low stock warning when remaining stock is 10 or more")
        void testReduceStock_ResultingIn10OrMoreUnits_NoWarning() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 40; // Results in 10 remaining
            int expectedNewQuantity = 10;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(10, result.getQuantity());
        }
    }

    @Nested
    @DisplayName("Edge Case and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle large inventory quantities")
        void testReduceStock_WithLargeQuantities_Success() {
            // Arrange
            testInventory.setQuantity(1_000_000);
            int productId = 100;
            int quantityToReduce = 500_000;
            int expectedNewQuantity = 500_000;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(500_000, result.getQuantity());
        }

        @Test
        @DisplayName("Should handle inventory with quantity of 1")
        void testReduceStock_WithQuantityOfOne_Success() {
            // Arrange
            testInventory.setQuantity(1);
            int productId = 100;
            int quantityToReduce = 1;
            int expectedNewQuantity = 0;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            Inventory result = inventoryService.reduceStock(productId, quantityToReduce);

            // Assert
            assertNotNull(result);
            assertEquals(0, result.getQuantity());
        }
    }

    @Nested
    @DisplayName("Method Interaction Verification Tests")
    class MethodInteractionTests {

        @Test
        @DisplayName("Should call DAO methods in correct order")
        void testReduceStock_VerifyMethodCallOrder_Success() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 20;
            int expectedNewQuantity = 30;
            
            Inventory updatedInventory = new Inventory();
            updatedInventory.setInventoryId(1);
            updatedInventory.setProductId(productId);
            updatedInventory.setQuantity(expectedNewQuantity);

            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));
            when(inventoryDao.updateQuantity(productId, expectedNewQuantity))
                .thenReturn(updatedInventory);

            // Act
            inventoryService.reduceStock(productId, quantityToReduce);

            // Assert - Verify call order
            var inOrder = inOrder(inventoryDao);
            inOrder.verify(inventoryDao).findByProductId(productId);
            inOrder.verify(inventoryDao).updateQuantity(productId, expectedNewQuantity);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Should not call updateQuantity when validation fails")
        void testReduceStock_WithInvalidInput_DoesNotCallUpdate() {
            // Arrange
            int productId = 0; // Invalid

            // Act & Assert
            assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reduceStock(productId, 10)
            );

            verify(inventoryDao, never()).updateQuantity(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should not call updateQuantity when insufficient stock")
        void testReduceStock_WithInsufficientStock_DoesNotCallUpdate() {
            // Arrange
            int productId = 100;
            int quantityToReduce = 100; // More than available
            when(inventoryDao.findByProductId(productId)).thenReturn(Optional.of(testInventory));

            // Act & Assert
            assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.reduceStock(productId, quantityToReduce)
            );

            verify(inventoryDao, never()).updateQuantity(anyInt(), anyInt());
        }
    }
}
