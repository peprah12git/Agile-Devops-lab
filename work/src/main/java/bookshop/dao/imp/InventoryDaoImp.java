package bookshop.dao.imp;

import bookshop.dao.daoInterface.InventoryDao;
import bookshop.models.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class InventoryDaoImp implements InventoryDao {
    
    private final DataSource datasource;

    @Autowired
    public InventoryDaoImp(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Inventory save(Inventory inventory) {
        //  NO database name
        String sql = "INSERT INTO inventory (product_id, quantity) VALUES (?, ?)";

        try (Connection connection = datasource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, inventory.getProductId());
            pstm.setInt(2, inventory.getQuantity());

            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    inventory.setInventoryId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving inventory", e);
        }

        return findByProductId(inventory.getProductId()).orElseThrow(() ->  new RuntimeException("Inventory not found after save"));
    }

    @Override
    public Optional<Inventory> findByProductId(int productId) {
        //  NO database name - includes JOIN with products
        String sql = """
                SELECT i.inventory_id,
                       i.product_id,
                       p.name AS product_name,
                       i.quantity,
                       i.last_updated
                FROM inventory i
                JOIN products p ON i.product_id = p.product_id
                WHERE i.product_id = ?
                """;

        try (Connection connection = datasource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, productId);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding inventory by productId", e);
        }

        return Optional.empty();
    }

    @Override
    public Inventory updateQuantity(int productId, int quantity) {
        // ✅ NO database name
        String sql = """
                UPDATE inventory
                SET quantity = ?,
                    last_updated = CURRENT_TIMESTAMP
                WHERE product_id = ?
                """;

        try (Connection connection = datasource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, quantity);
            pstm.setInt(2, productId);

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("Inventory not found for productId " + productId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating inventory quantity", e);
        }

        return findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found after update"));
    }

    @Override
    public void deleteByProductId(int productId) {
        // ✅ NO database name
        String sql = "DELETE FROM inventory WHERE product_id = ?";

        try (Connection connection = datasource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, productId);

            int rowsDeleted = pstm.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("Inventory not found for productId " + productId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting inventory", e);
        }
    }

    private Inventory mapRow(ResultSet rs) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setProductId(rs.getInt("product_id"));
       // inventory.setName(rs.getString("product_name"));
        inventory.setQuantity(rs.getInt("quantity"));
        inventory.setLastUpdated(rs.getTimestamp("last_updated"));
        return inventory;
    }
}