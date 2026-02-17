package bookshop.dto.response;

import java.sql.Timestamp;

/**
 * DTO for inventory response
 */
public class InventoryResponseDto {

    private int inventoryId;
    private int productId;
    private String productName;
    private int quantityAvailable;
    private Timestamp lastUpdated;
    private boolean inStock;

    // Constructors
    public InventoryResponseDto() {
    }

    public InventoryResponseDto(int inventoryId, int productId, String productName, 
                                int quantityAvailable, Timestamp lastUpdated, boolean inStock) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.productName = productName;
        this.quantityAvailable = quantityAvailable;
        this.lastUpdated = lastUpdated;
        this.inStock = inStock;
    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    @Override
    public String toString() {
        return "InventoryResponseDto{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                ", lastUpdated=" + lastUpdated +
                ", inStock=" + inStock +
                '}';
    }
}