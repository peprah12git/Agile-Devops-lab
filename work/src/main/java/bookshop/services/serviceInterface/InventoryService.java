package bookshop.services.serviceInterface;

import bookshop.models.Inventory;
import java.util.Optional;

/**
 * Service interface for Inventory business logic operations
 */
public interface InventoryService {

    /**
     * Create a new inventory record for a product
     * @param inventory the inventory to create
     * @return the created inventory with generated ID
     */
    Inventory createInventory(Inventory inventory);

    /**
     * Get inventory information for a specific product
     * @param productId the product ID
     * @return Optional containing the inventory if found
     */
    Optional<Inventory> getInventoryByProductId(int productId);

    /**
     * Update the quantity for a product
     * @param productId the product ID
     * @param quantity the new quantity
     * @return the updated inventory
     */
    Inventory updateQuantity(int productId, int quantity);

    /**
     * Add stock to existing inventory
     * @param productId the product ID
     * @param quantityToAdd the quantity to add
     * @return the updated inventory
     */
    Inventory addStock(int productId, int quantityToAdd);

    /**
     * Reduce stock from existing inventory (for orders)
     * @param productId the product ID
     * @param quantityToReduce the quantity to reduce
     * @return the updated inventory
     */
    Inventory reduceStock(int productId, int quantityToReduce);

    /**
     * Check if a product has sufficient stock
     * @param productId the product ID
     * @param requiredQuantity the required quantity
     * @return true if sufficient stock available, false otherwise
     */
    boolean hasStock(int productId, int requiredQuantity);

    /**
     * Check if a product is in stock
     * @param productId the product ID
     * @return true if product has stock > 0, false otherwise
     */
    boolean isInStock(int productId);

    /**
     * Delete inventory record for a product
     * @param productId the product ID
     */
    void deleteInventory(int productId);

    /**
     * Get available quantity for a product
     * @param productId the product ID
     * @return the available quantity, or 0 if not found
     */
    int getAvailableQuantity(int productId);
}