package bookshop.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bookshop.dao.daoInterface.CategoryDao;
import bookshop.models.Category;

@Repository
public class CategoryDaoImp implements CategoryDao {
    
    private final DataSource dataSource;

    @Autowired
    public CategoryDaoImp(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Category save(Category category) {
        // ✅ NO database name - just table name
        String sql = "INSERT INTO category (name) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, category.getName());
            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setCategoryId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving category", e);
        }

        return category;
    }

    @Override
    public List<Category> findAll() {
        // ✅ NO database name
        String sql = "SELECT * FROM category";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                categories.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching categories", e);
        }

        return categories;
    }

    @Override
    public Optional<Category> findById(int categoryId) {
        // ✅ NO database name
        String sql = "SELECT * FROM category WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, categoryId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by id", e);
        }

        return Optional.empty();
    }

    @Override
    public Category update(Category category) {
        // ✅ NO database name
        String sql = "UPDATE category SET name = ? WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, category.getName());
            pstm.setInt(2, category.getCategoryId());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("Category not found with id " + category.getCategoryId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }

        return category;
    }

    @Override
    public void deleteById(int categoryId) {
        // ✅ NO database name
        String sql = "DELETE FROM category WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, categoryId);

            int rowsDeleted = pstm.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("Category not found with id " + categoryId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    @Override
    public boolean existsById(int categoryId) {
        // ✅ NO database name
        String sql = "SELECT COUNT(*) FROM category WHERE category_id = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, categoryId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking category existence", e);
        }

        return false;
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        return category;
    }
}