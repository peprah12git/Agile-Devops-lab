package bookshop.controller;

import bookshop.exceptions.CategoryNotFoundException;
import bookshop.models.Category;
import bookshop.services.serviceInterface.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category operations
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * Get all categories
     * GET /api/categories
     * 
     * @return list of all categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        logger.info("GET /api/categories - Fetching all categories");
        List<Category> categories = categoryService.getAllCategories();
        logger.debug("Retrieved {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    /**
     * Get category by ID
     * GET /api/categories/{id}
     * 
     * @param id the category ID
     * @return the category
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        logger.info("GET /api/categories/{} - Fetching category", id);
        return categoryService.getCategoryById(id)
                .map(category -> {
                    logger.debug("Category found: {}", category.getName());
                    return ResponseEntity.ok(category);
                })
                .orElseGet(() -> {
                    logger.warn("Category not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Create a new category
     * POST /api/categories
     * 
     * @param category the category to create
     * @return the created category with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        logger.info("POST /api/categories - Creating category: {}", category.getName());
        Category created = categoryService.createCategory(category);
        logger.info("Category created with ID: {}", created.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update a category
     * PUT /api/categories/{id}
     * 
     * @param id the category ID
     * @param category the updated category data
     * @return the updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody Category category) {
        
        logger.info("PUT /api/categories/{} - Updating category", id);
        
        // Verify category exists
        categoryService.getCategoryById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", id);
                    return new CategoryNotFoundException("Category not found with ID: " + id);
                });
        
        category.setCategoryId(id);
        Category updated = categoryService.updateCategory(category);
        logger.info("Category {} updated successfully", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a category
     * DELETE /api/categories/{id}
     * 
     * @param id the category ID to delete
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        logger.info("DELETE /api/categories/{} - Deleting category", id);
        categoryService.deleteCategory(id);
        logger.info("Category {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
