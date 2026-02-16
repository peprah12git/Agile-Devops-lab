package bookshop.dao.daoInterface;

import bookshop.models.Inventory;
import java.util.Optional;

public interface InventoryDao {

    // Create
    Inventory save(Inventory inventory);

    // Read
    Optional<Inventory> findByProductId(int productId);

    // Update stock
    Inventory updateQuantity(int productId, int quantity);

    // Delete
    void deleteByProductId(int productId);
}
