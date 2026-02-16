package bookshop.dao.daoInterface;

import bookshop.models.Category;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Category operations
 */
public interface CategoryDao {

    /**
     * Save a new category
     */
    Category save(Category category);

    /**
     * Find all categories
     */
    List<Category> findAll();

    /**
     * Find category by ID
     * @param categoryId the category ID
     * @return Optional containing the category if found
     */
    Optional<Category> findById(int categoryId);

    /**
     * Update an existing category
     */
    Category update(Category category);

    /**
     * Delete category by ID
     */
    void deleteById(int categoryId);

    /**
     * Check if a category exists by ID
     * @param categoryId the category ID to check
     * @return true if category exists, false otherwise
     */
    boolean existsById(int categoryId);
}