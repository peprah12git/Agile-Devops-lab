package bookshop.services.serviceimp;

import bookshop.dao.daoInterface.CategoryDao;
import bookshop.exceptions.CategoryNotFoundException;
import bookshop.models.Category;
import bookshop.services.serviceInterface.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of CategoryService
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Category createCategory(Category category) {
        logger.info("Creating new category: {}", category.getName());
        Category created = categoryDao.save(category);
        logger.info("Category created with ID: {}", created.getCategoryId());
        return created;
    }

    @Override
    public List<Category> getAllCategories() {
        logger.debug("Fetching all categories");
        List<Category> categories = categoryDao.findAll();
        logger.debug("Retrieved {} categories", categories.size());
        return categories;
    }

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        logger.debug("Fetching category with ID: {}", categoryId);
        return categoryDao.findById(categoryId);
    }

    @Override
    public Category updateCategory(Category category) {
        logger.info("Updating category with ID: {}", category.getCategoryId());
        
        // Verify category exists
        if (!categoryDao.existsById(category.getCategoryId())) {
            logger.error("Category not found with ID: {}", category.getCategoryId());
            throw new CategoryNotFoundException("Category not found with ID: " + category.getCategoryId());
        }
        
        Category updated = categoryDao.update(category);
        logger.info("Category {} updated successfully", category.getCategoryId());
        return updated;
    }

    @Override
    public void deleteCategory(int categoryId) {
        logger.info("Deleting category with ID: {}", categoryId);
        
        // Verify category exists
        if (!categoryDao.existsById(categoryId)) {
            logger.error("Category not found with ID: {}", categoryId);
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        
        categoryDao.deleteById(categoryId);
        logger.info("Category {} deleted successfully", categoryId);
    }
}
