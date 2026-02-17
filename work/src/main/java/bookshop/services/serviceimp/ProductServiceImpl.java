package bookshop.services.serviceimp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import bookshop.dao.daoInterface.CategoryDao;
import bookshop.dao.daoInterface.ProductDao;
import bookshop.dto.request.PageRequest;
import bookshop.dto.response.PageResponse;
import bookshop.exceptions.BusinessException;
import bookshop.exceptions.ProductNotFoundException;
import bookshop.models.Product;
import bookshop.services.serviceInterface.ProductService;

/**
 * Implementation of ProductService with business logic, validation, and pagination
 */
@Service
@Validated
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    // ========== CREATE ==========

    @Override
    public Product createProduct(Product product) {
        logger.info("Creating product: {}", product.getName());

        // Bean validation is automatic with @Valid

        // Business Validation: Category must exist
        if (!categoryDao.existsById(product.getCategoryId())) {
            logger.error("Category not found: {}", product.getCategoryId());
            throw new BusinessException("Category with ID " + product.getCategoryId() + " does not exist");
        }

        // Business Validation: Product name must be unique
        if (productDao.existsByName(product.getName())) {
            logger.error("Product name already exists: {}", product.getName());
            throw new BusinessException("Product name '" + product.getName() + "' already exists");
        }

        // Business Validation: Price must be positive
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be greater than zero");
        }

        // Save product to database
        Product savedProduct = productDao.save(product);
        logger.info("Successfully created product with ID: {}", savedProduct.getProductId());

        return savedProduct;
    }

    // ========== READ (PAGINATED) ==========

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Product> getAllProducts(PageRequest pageRequest) {
        logger.debug("Fetching all products with pagination: {}", pageRequest);

        // Validate page request
        validatePageRequest(pageRequest);

        // Get paginated data
        List<Product> products = productDao.findAll(pageRequest);

        // Get total count
        long totalElements = productDao.count();

        logger.debug("Found {} products on page {}, total: {}",
                products.size(), pageRequest.getPage(), totalElements);

        return new PageResponse<>(products, pageRequest.getPage(), pageRequest.getSize(), totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Product> getProductsByCategory(int categoryId, PageRequest pageRequest) {
        logger.debug("Fetching products for category {} with pagination: {}", categoryId, pageRequest);

        // Validate inputs
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be positive");
        }
        validatePageRequest(pageRequest);

        // Verify category exists
        if (!categoryDao.existsById(categoryId)) {
            logger.error("Category not found: {}", categoryId);
            throw new BusinessException("Category with ID " + categoryId + " does not exist");
        }

        // Get paginated data
        List<Product> products = productDao.findByCategoryId(categoryId, pageRequest);

        // Get total count for this category
        long totalElements = productDao.countByCategory(categoryId);

        logger.debug("Found {} products in category {} on page {}, total: {}",
                products.size(), categoryId, pageRequest.getPage(), totalElements);

        return new PageResponse<>(products, pageRequest.getPage(), pageRequest.getSize(), totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Product> searchProducts(String keyword, PageRequest pageRequest) {
        logger.debug("Searching products with keyword '{}' and pagination: {}", keyword, pageRequest);

        // Validate inputs
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty");
        }
        validatePageRequest(pageRequest);

        // Get paginated data
        List<Product> products = productDao.searchByName(keyword, pageRequest);

        // Get total count for this search
        long totalElements = productDao.countByNameSearch(keyword);

        logger.debug("Found {} products matching '{}' on page {}, total: {}",
                products.size(), keyword, pageRequest.getPage(), totalElements);

        return new PageResponse<>(products, pageRequest.getPage(), pageRequest.getSize(), totalElements);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest) {
        logger.debug("Fetching products in price range {} - {} with pagination: {}", minPrice, maxPrice, pageRequest);

        // Validate inputs
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range cannot be null");
        }
        if (minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }
        if (maxPrice.compareTo(minPrice) < 0) {
            throw new IllegalArgumentException("Maximum price cannot be less than minimum price");
        }
        validatePageRequest(pageRequest);

        // Get paginated data
        List<Product> products = productDao.findByPriceRange(minPrice, maxPrice, pageRequest);

        // Get total count for this price range
        long totalElements = productDao.countByPriceRange(minPrice, maxPrice);

        logger.debug("Found {} products in price range on page {}, total: {}",
                products.size(), pageRequest.getPage(), totalElements);

        return new PageResponse<>(products, pageRequest.getPage(), pageRequest.getSize(), totalElements);
    }

    // ========== READ (NON-PAGINATED) ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(int productId) {
        if (productId <= 0) {
            logger.error("Invalid product ID: {}", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        logger.debug("Fetching product with ID: {}", productId);
        return productDao.findById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        List<Product> products = productDao.findAll();
        logger.debug("Found {} products", products.size());
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be positive");
        }

        // Verify category exists
        if (!categoryDao.existsById(categoryId)) {
            logger.error("Category not found: {}", categoryId);
            throw new BusinessException("Category with ID " + categoryId + " does not exist");
        }

        logger.debug("Fetching products for category ID: {}", categoryId);
        List<Product> products = productDao.findByCategoryId(categoryId);
        logger.debug("Found {} products in category {}", products.size(), categoryId);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty");
        }

        logger.debug("Searching products with keyword: {}", keyword);
        List<Product> products = productDao.searchByName(keyword);
        logger.debug("Found {} products matching '{}'", products.size(), keyword);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range cannot be null");
        }

        if (minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }

        if (maxPrice.compareTo(minPrice) < 0) {
            throw new IllegalArgumentException("Maximum price cannot be less than minimum price");
        }

        logger.debug("Fetching products in price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productDao.findByPriceRange(minPrice, maxPrice);
        logger.debug("Found {} products in price range", products.size());
        return products;
    }

    // ========== UPDATE ==========

    @Override
    public Product updateProduct(Product product) {
        logger.info("Updating product with ID: {}", product.getProductId());

        // Verify product exists
        Optional<Product> existingProduct = productDao.findById(product.getProductId());
        if (existingProduct.isEmpty()) {
            logger.error("Product not found with ID: {}", product.getProductId());
            throw new ProductNotFoundException(product.getProductId());
        }

        // Verify category exists
        if (!categoryDao.existsById(product.getCategoryId())) {
            logger.error("Category not found: {}", product.getCategoryId());
            throw new BusinessException("Category with ID " + product.getCategoryId() + " does not exist");
        }

        // Check if name is being changed to an existing name
        if (productNameExists(product.getName(), product.getProductId())) {
            logger.error("Product name already exists: {}", product.getName());
            throw new BusinessException("Product name '" + product.getName() + "' already exists");
        }

        // Validate price
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be greater than zero");
        }

        Product updatedProduct = productDao.update(product);
        logger.info("Successfully updated product with ID: {}", updatedProduct.getProductId());

        return updatedProduct;
    }

    @Override
    public Product updateProductPrice(int productId, BigDecimal newPrice) {
        logger.info("Updating price for product ID: {} to {}", productId, newPrice);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        // Get existing product
        Product product = productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        BigDecimal oldPrice = product.getPrice();

        // Update price
        product.setPrice(newPrice);
        Product updatedProduct = productDao.update(product);

        logger.info("Successfully updated price for product ID: {} from {} to {}",
                productId, oldPrice, newPrice);

        return updatedProduct;
    }

    // ========== DELETE ==========

    @Override
    public void deleteProduct(int productId) {
        logger.info("Deleting product with ID: {}", productId);

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }

        // Verify product exists
        Optional<Product> product = productDao.findById(productId);
        if (product.isEmpty()) {
            logger.error("Product not found with ID: {}", productId);
            throw new ProductNotFoundException(productId);
        }

        productDao.deleteById(productId);
        logger.info("Successfully deleted product with ID: {}", productId);
    }

    // ========== BUSINESS LOGIC ==========

    @Override
    @Transactional(readOnly = true)
    public boolean productExists(int productId) {
        if (productId <= 0) {
            return false;
        }
        return productDao.findById(productId).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean productNameExists(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }
        return productDao.existsByName(productName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean productNameExists(String productName, int excludeProductId) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }

        // Check if any other product has this name
        Optional<Product> existingProduct = productDao.findByName(productName);

        if (existingProduct.isEmpty()) {
            return false;
        }

        // Name exists, but check if it's the same product we're updating
        return existingProduct.get().getProductId() != excludeProductId;
    }

    @Override
    @Transactional(readOnly = true)
    public int getProductCountByCategory(int categoryId) {
        if (categoryId <= 0) {
            return 0;
        }

        return productDao.findByCategoryId(categoryId).size();
    }

    // ========== HELPER METHODS ==========

    /**
     * Validate PageRequest parameters
     */
    private void validatePageRequest(PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new IllegalArgumentException("PageRequest cannot be null");
        }
        if (pageRequest.getPage() < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (pageRequest.getSize() <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (pageRequest.getSize() > 100) {
            throw new IllegalArgumentException("Page size cannot exceed 100");
        }
    }
}