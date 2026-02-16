package bookshop.exceptions;

/**
 * Exception thrown when inventory is not found for a product
 */
public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InventoryNotFoundException(int productId) {
        super("Inventory not found for product ID: " + productId);
    }
}