package bookshop.dao.daoInterface;

import bookshop.dto.request.PageRequest;
import bookshop.models.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product operations
 * Now with pagination support
 */
public interface ProductDao {

    // ========== CREATE ==========
    Product save(Product product);

    // ========== READ (Basic) ==========
    List<Product> findAll();
    Optional<Product> findById(int productId);
    Optional<Product> findByName(String productName);

    // ========== READ (Paginated) ==========

    /**
     * Find all products with pagination
     * @param pageRequest pagination parameters
     * @return list of products for the requested page
     */
    List<Product> findAll(PageRequest pageRequest);

    /**
     * Find products by category with pagination
     * @param categoryId the category ID
     * @param pageRequest pagination parameters
     * @return list of products for the requested page
     */
    List<Product> findByCategoryId(int categoryId, PageRequest pageRequest);

    /**
     * Search products by name with pagination
     * @param keyword the search keyword
     * @param pageRequest pagination parameters
     * @return list of matching products for the requested page
     */
    List<Product> searchByName(String keyword, PageRequest pageRequest);

    /**
     * Find products by price range with pagination
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param pageRequest pagination parameters
     * @return list of products in price range for the requested page
     */
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest);

    // ========== COUNT (for pagination) ==========

    /**
     * Count total products
     * @return total number of products
     */
    long count();

    /**
     * Count products by category
     * @param categoryId the category ID
     * @return number of products in category
     */
    long countByCategory(int categoryId);

    /**
     * Count products matching search keyword
     * @param keyword the search keyword
     * @return number of matching products
     */
    long countByNameSearch(String keyword);

    /**
     * Count products in price range
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return number of products in range
     */
    long countByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // ========== READ (Non-paginated - for backward compatibility) ==========
    List<Product> findByCategoryId(int categoryId);
    List<Product> searchByName(String keyword);
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // ========== UPDATE ==========
    Product update(Product product);

    // ========== DELETE ==========
    void deleteById(int productId);

    // ========== UTILITY ==========
    boolean existsByName(String productName);
}