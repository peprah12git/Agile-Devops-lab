package bookshop.services.serviceInterface;

import bookshop.models.Category;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Category business logic
 */
public interface CategoryService {

    /**
     * Create a new category
     * @param category the category to create
     * @return the created category
     */
    Category createCategory(Category category);

    /**
     * Get all categories
     * @return list of all categories
     */
    List<Category> getAllCategories();

    /**
     * Get category by ID
     * @param categoryId the category ID
     * @return Optional containing the category if found
     */
    Optional<Category> getCategoryById(int categoryId);

    /**
     * Update a category
     * @param category the category to update
     * @return the updated category
     */
    Category updateCategory(Category category);

    /**
     * Delete category by ID
     * @param categoryId the category ID to delete
     */
    void deleteCategory(int categoryId);
}
