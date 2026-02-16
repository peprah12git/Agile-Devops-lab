package bookshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * DTO for inventory response
 */
@Data
@AllArgsConstructor
public class InventoryResponseDto {

    private int inventoryId;
    private int productId;
    private String productName;
    private int quantityAvailable;
    private Timestamp lastUpdated;
    private boolean inStock;


}