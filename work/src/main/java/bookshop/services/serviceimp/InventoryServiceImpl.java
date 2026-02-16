package bookshop.services.serviceimp;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookshop.dao.daoInterface.InventoryDao;
import bookshop.exceptions.InsufficientStockException;
import bookshop.exceptions.InventoryNotFoundException;
import bookshop.models.Inventory;
import bookshop.services.serviceInterface.InventoryService;

/**
 * Implementation of InventoryService with business logic and validation
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryDao inventoryDao;

    @Autowired
    public InventoryServiceImpl(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        logger.info("Creating inventory for product ID: {}", inventory.getProductId());

        validateInventory(inventory);

        // Check if inventory already exists for this product
        Optional<Inventory> existing = inventoryDao.findByProductId(inventory.getProductId());
        if (existing.isPresent()) {
            logger.warn("Inventory already exists for product ID: {}", inventory.getProductId());
            throw new IllegalArgumentException(
                    "Inventory already exists for product ID: " + inventory.getProductId()
            );
        }

        // Ensure quantity is not negative
        if (inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Inventory savedInventory = inventoryDao.save(inventory);
        logger.info("Successfully created inventory with ID: {}", savedInventory.getInventoryId());

        return savedInventory;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> getInventoryByProductId(int productId) {
        if (productId <= 0) {
            logger.error("Invalid product ID: {}", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        logger.debug("Fetching inventory for product ID: {}", productId);
        return inventoryDao.findByProductId(productId);
    }

    @Override
    public Inventory updateQuantity(int productId, int quantity) {
        logger.info("Updating quantity for product ID: {} to {}", productId, quantity);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        // Verify inventory exists
        Optional<Inventory> existing = inventoryDao.findByProductId(productId);
        if (existing.isEmpty()) {
            logger.error("Inventory not found for product ID: {}", productId);
            throw new InventoryNotFoundException(productId);
        }

        Inventory updated = inventoryDao.updateQuantity(productId, quantity);
        logger.info("Successfully updated quantity for product ID: {}", productId);

        return updated;
    }

    @Override
    public Inventory addStock(int productId, int quantityToAdd) {
        logger.info("Adding {} units to product ID: {}", quantityToAdd, productId);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive");
        }

        // Get current inventory
        Inventory inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));

        // Calculate new quantity
        int newQuantity = inventory.getQuantity() + quantityToAdd;

        // Update inventory
        Inventory updated = inventoryDao.updateQuantity(productId, newQuantity);
        logger.info("Successfully added {} units to product ID: {}. New quantity: {}",
                quantityToAdd, productId, newQuantity);

        return updated;
    }

    @Override
    public Inventory reduceStock(int productId, int quantityToReduce) {
        logger.info("Reducing {} units from product ID: {}", quantityToReduce, productId);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (quantityToReduce <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be positive");
        }

        // Get current inventory
        Inventory inventory = inventoryDao.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));

        // Check if sufficient stock is available
        if (inventory.getQuantity() < quantityToReduce) {
            logger.warn("Insufficient stock for product ID: {}. Available: {}, Required: {}",
                    productId, inventory.getQuantity(), quantityToReduce);
            throw new InsufficientStockException(
                    productId,
                    inventory.getQuantity(),
                    quantityToReduce
            );
        }

        // Calculate new quantity
        int newQuantity = inventory.getQuantity() - quantityToReduce;

        // Update inventory
        Inventory updated = inventoryDao.updateQuantity(productId, newQuantity);
        logger.info("Successfully reduced {} units from product ID: {}. New quantity: {}",
                quantityToReduce, productId, newQuantity);

        // Log low stock warning
        if (newQuantity < 10) {
            logger.warn("Low stock alert for product ID: {}. Only {} units remaining",
                    productId, newQuantity);
        }

        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasStock(int productId, int requiredQuantity) {
        if (productId <= 0 || requiredQuantity <= 0) {
            return false;
        }

        Optional<Inventory> inventory = inventoryDao.findByProductId(productId);

        if (inventory.isEmpty()) {
            logger.debug("No inventory found for product ID: {}", productId);
            return false;
        }

        boolean hasEnoughStock = inventory.get().getQuantity() >= requiredQuantity;

        if (!hasEnoughStock) {
            logger.debug("Insufficient stock for product ID: {}. Available: {}, Required: {}",
                    productId, inventory.get().getQuantity(), requiredQuantity);
        }

        return hasEnoughStock;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(int productId) {
        if (productId <= 0) {
            return false;
        }

        Optional<Inventory> inventory = inventoryDao.findByProductId(productId);
        return inventory.isPresent() && inventory.get().getQuantity() > 0;
    }

    @Override
    public void deleteInventory(int productId) {
        logger.info("Deleting inventory for product ID: {}", productId);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        // Verify inventory exists
        Optional<Inventory> existing = inventoryDao.findByProductId(productId);
        if (existing.isEmpty()) {
            logger.error("Inventory not found for product ID: {}", productId);
            throw new InventoryNotFoundException(productId);
        }

        inventoryDao.deleteByProductId(productId);
        logger.info("Successfully deleted inventory for product ID: {}", productId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getAvailableQuantity(int productId) {
        if (productId <= 0) {
            return 0;
        }

        Optional<Inventory> inventory = inventoryDao.findByProductId(productId);
        return inventory.map(Inventory::getQuantity).orElse(0);
    }

    /**
     * Validate inventory object
     * @param inventory the inventory to validate
     */
    private void validateInventory(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }

        if (inventory.getProductId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
    }
}