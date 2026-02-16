package bookshop.models;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Inventory {
    private int inventoryId;
    private int productId;
    private int quantity;
    private Timestamp lastUpdated;
}
