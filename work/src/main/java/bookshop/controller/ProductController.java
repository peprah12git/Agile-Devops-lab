package bookshop.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bookshop.dto.request.PageRequest;
import bookshop.dto.request.ProductCreateDto;
import bookshop.dto.request.ProductUpdateDto;
import bookshop.dto.response.PageResponse;
import bookshop.exceptions.ProductNotFoundException;
import bookshop.models.Product;
import bookshop.services.serviceInterface.ProductService;
import jakarta.validation.Valid;

/**
 * REST Controller for Product operations with pagination
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products with pagination and sorting
     *
     * GET /api/products?page=0&size=20&sortBy=name&direction=ASC
     *
     * @param page page number (default 0)
     * @param size items per page (default 20, max 100)
     * @param sortBy field to sort by (default: productId)
     * @param direction sort direction ASC or DESC (default: ASC)
     * @return paginated list of products
     */
    @GetMapping
    public ResponseEntity<PageResponse<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, direction);
        PageResponse<Product> response = productService.getAllProducts(pageRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Get single product by ID
     *
     * GET /api/products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get products by category with pagination
     *
     * GET /api/products/category/1?page=0&size=10&sortBy=price&direction=DESC
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponse<Product>> getProductsByCategory(
            @PathVariable int categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, direction);
        PageResponse<Product> response = productService.getProductsByCategory(categoryId, pageRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Search products by name with pagination
     *
     * GET /api/products/search?keyword=book&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, direction);
        PageResponse<Product> response = productService.searchProducts(keyword, pageRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Get products by price range with pagination
     *
     * GET /api/products/price-range?minPrice=10&maxPrice=50&page=0&size=10&sortBy=price&direction=ASC
     */
    @GetMapping("/price-range")
    public ResponseEntity<PageResponse<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, direction);
        PageResponse<Product> response = productService.getProductsByPriceRange(minPrice, maxPrice, pageRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Create a new product
     *
     * POST /api/products
     * 
     * @param productCreateDto the product data to create
     * @return the created product with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Product>
    createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        // Map DTO to Product model
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setPrice(productCreateDto.getPrice());
        product.setCategoryId(productCreateDto.getCategoryId());
        
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update a product
     *
     * PUT /api/products/{id}
     * 
     * @param id the product ID to update
     * @param productUpdateDto the updated product data
     * @return the updated product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductUpdateDto productUpdateDto) {

        // Verify product exists
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        
        // Update fields from DTO
        existingProduct.setName(productUpdateDto.getName());
        existingProduct.setPrice(productUpdateDto.getPrice());
        existingProduct.setCategoryId(productUpdateDto.getCategoryId());
        
        Product updated = productService.updateProduct(existingProduct);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a product
     *
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}