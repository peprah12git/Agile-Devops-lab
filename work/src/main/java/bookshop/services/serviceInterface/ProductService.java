package bookshop.services.serviceInterface;

import bookshop.dto.request.PageRequest;
import bookshop.dto.response.PageResponse;
import bookshop.models.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Product business logic operations
 * Now with pagination support
 */
public interface ProductService {

    // ========== CREATE ==========
    Product createProduct(Product product);

    // ========== READ (Paginated) ==========

    /**
     * Get all products with pagination
     * @param pageRequest pagination parameters
     * @return paginated response with products
     */
    PageResponse<Product> getAllProducts(PageRequest pageRequest);

    /**
     * Get products by category with pagination
     * @param categoryId the category ID
     * @param pageRequest pagination parameters
     * @return paginated response with products
     */
    PageResponse<Product> getProductsByCategory(int categoryId, PageRequest pageRequest);

    /**
     * Search products by name with pagination
     * @param keyword the search keyword
     * @param pageRequest pagination parameters
     * @return paginated response with matching products
     */
    PageResponse<Product> searchProducts(String keyword, PageRequest pageRequest);

    /**
     * Get products within price range with pagination
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param pageRequest pagination parameters
     * @return paginated response with products in range
     */
    PageResponse<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest);

    // ========== READ (Non-paginated) ==========
    Optional<Product> getProductById(int productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(int categoryId);
    List<Product> searchProducts(String keyword);
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // ========== UPDATE ==========
    Product updateProduct(Product product);
    Product updateProductPrice(int productId, BigDecimal newPrice);

    // ========== DELETE ==========
    void deleteProduct(int productId);

    // ========== BUSINESS LOGIC ==========
    boolean productExists(int productId);
    boolean productNameExists(String productName);
    boolean productNameExists(String productName, int excludeProductId);
    int getProductCountByCategory(int categoryId);
}