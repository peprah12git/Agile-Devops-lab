package bookshop.exceptions;

/**
 * Exception thrown when trying to reduce stock but insufficient quantity is available
 */
public class InsufficientStockException extends RuntimeException {

    private final int productId;
    private final int availableQuantity;
    private final int requiredQuantity;

    public InsufficientStockException(int productId, int availableQuantity, int requiredQuantity) {
        super(String.format(
                "Insufficient stock for product ID: %d. Available: %d, Required: %d",
                productId, availableQuantity, requiredQuantity
        ));
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.requiredQuantity = requiredQuantity;
    }

    public InsufficientStockException(String message) {
        super(message);
        this.productId = 0;
        this.availableQuantity = 0;
        this.requiredQuantity = 0;
    }

    public int getProductId() {
        return productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }
}