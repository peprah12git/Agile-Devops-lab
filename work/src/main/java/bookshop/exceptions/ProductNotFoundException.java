package bookshop.exceptions;

/**
 * Exception thrown when a product is not found
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(int productId) {
        super("Product not found with ID: " + productId);
    }
}