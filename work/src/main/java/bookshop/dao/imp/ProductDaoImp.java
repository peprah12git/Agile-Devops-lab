package bookshop.dao.imp;

import bookshop.dao.daoInterface.ProductDao;
import bookshop.dto.request.PageRequest;
import bookshop.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDaoImp implements ProductDao {

    private final DataSource dataSource;

    @Autowired
    public ProductDaoImp(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ========== CREATE ==========

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (name, price, category_id) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, product.getName());
            pstm.setBigDecimal(2, product.getPrice());
            pstm.setInt(3, product.getCategoryId());

            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }

        return product;
    }

    // ========== READ (Paginated) ==========

    @Override
    public List<Product> findAll(PageRequest pageRequest) {
        String sql = buildPaginatedQuery(
                "SELECT p.*, c.name AS category_name FROM products p " +
                        "LEFT JOIN category c ON p.category_id = c.category_id",
                pageRequest
        );

        return executeQuery(sql, pageRequest);
    }

    @Override
    public List<Product> findByCategoryId(int categoryId, PageRequest pageRequest) {
        String baseSql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.category_id = ?";

        String sql = buildPaginatedQuery(baseSql, pageRequest);

        return executeQueryWithParam(sql, categoryId, pageRequest);
    }

    @Override
    public List<Product> searchByName(String keyword, PageRequest pageRequest) {
        String baseSql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.name LIKE ?";

        String sql = buildPaginatedQuery(baseSql, pageRequest);

        return executeQueryWithParam(sql, "%" + keyword + "%", pageRequest);
    }

    @Override
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest) {
        String baseSql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.price BETWEEN ? AND ?";

        String sql = buildPaginatedQuery(baseSql, pageRequest);

        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setBigDecimal(1, minPrice);
            pstm.setBigDecimal(2, maxPrice);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by price range", e);
        }

        return products;
    }

    // ========== COUNT METHODS ==========

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM products";
        return executeCount(sql);
    }

    @Override
    public long countByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, categoryId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting products by category", e);
        }

        return 0;
    }

    @Override
    public long countByNameSearch(String keyword) {
        String sql = "SELECT COUNT(*) FROM products WHERE name LIKE ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting products by search", e);
        }

        return 0;
    }

    @Override
    public long countByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT COUNT(*) FROM products WHERE price BETWEEN ? AND ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setBigDecimal(1, minPrice);
            pstm.setBigDecimal(2, maxPrice);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting products by price range", e);
        }

        return 0;
    }

    // ========== READ (Non-paginated - backward compatibility) ==========

    @Override
    public List<Product> findAll() {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id";

        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products", e);
        }

        return products;
    }

    @Override
    public Optional<Product> findById(int productId) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, productId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by id", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Product> findByName(String productName) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, productName);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by name", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.category_id = ?";

        return executeQueryWithIntParam(sql, categoryId);
    }

    @Override
    public List<Product> searchByName(String keyword) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.name LIKE ?";

        return executeQueryWithStringParam(sql, "%" + keyword + "%");
    }

    @Override
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN category c ON p.category_id = c.category_id " +
                "WHERE p.price BETWEEN ? AND ?";

        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setBigDecimal(1, minPrice);
            pstm.setBigDecimal(2, maxPrice);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by price range", e);
        }

        return products;
    }

    // ========== UPDATE ==========

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, category_id = ? WHERE product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, product.getName());
            pstm.setBigDecimal(2, product.getPrice());
            pstm.setInt(3, product.getCategoryId());
            pstm.setInt(4, product.getProductId());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("Product not found with id " + product.getProductId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }

        return product;
    }

    // ========== DELETE ==========

    @Override
    public void deleteById(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, productId);

            int rowsDeleted = pstm.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("Product not found with id " + productId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    // ========== UTILITY ==========

    @Override
    public boolean existsByName(String productName) {
        String sql = "SELECT COUNT(*) FROM products WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, productName);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking product existence", e);
        }

        return false;
    }

    // ========== HELPER METHODS ==========

    /**
     * Build paginated SQL query with ORDER BY and LIMIT/OFFSET
     */
    private String buildPaginatedQuery(String baseSql, PageRequest pageRequest) {
        StringBuilder sql = new StringBuilder(baseSql);

        // Add ORDER BY clause
        if (pageRequest.getSortBy() != null && !pageRequest.getSortBy().isEmpty()) {
            sql.append(" ORDER BY ").append(sanitizeSortField(pageRequest.getSortBy()))
                    .append(" ").append(pageRequest.getDirection());
        } else {
            sql.append(" ORDER BY p.product_id ASC");  // Default sort
        }

        // Add LIMIT and OFFSET
        sql.append(" LIMIT ").append(pageRequest.getSize())
                .append(" OFFSET ").append(pageRequest.getOffset());

        return sql.toString();
    }

    /**
     * Sanitize sort field to prevent SQL injection
     */
    private String sanitizeSortField(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "name", "productname" -> "p.name";
            case "price" -> "p.price";
            case "categoryid" -> "p.category_id";
            case "productid", "id" -> "p.product_id";
            default -> "p.product_id";  // Default fallback
        };
    }

    /**
     * Execute query without parameters
     */
    private List<Product> executeQuery(String sql, PageRequest pageRequest) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return products;
    }

    /**
     * Execute query with one parameter (int or String)
     */
    private List<Product> executeQueryWithParam(String sql, Object param, PageRequest pageRequest) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            if (param instanceof Integer) {
                pstm.setInt(1, (Integer) param);
            } else if (param instanceof String) {
                pstm.setString(1, (String) param);
            }

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return products;
    }

    /**
     * Execute query with int parameter (non-paginated)
     */
    private List<Product> executeQueryWithIntParam(String sql, int param) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, param);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return products;
    }

    /**
     * Execute query with string parameter (non-paginated)
     */
    private List<Product> executeQueryWithStringParam(String sql, String param) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, param);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return products;
    }

    /**
     * Execute COUNT query
     */
    private long executeCount(String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error executing count", e);
        }

        return 0;
    }

    /**
     * Map ResultSet row to Product object
     */
    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setCategoryName(rs.getString("category_name"));
        return product;
    }
}