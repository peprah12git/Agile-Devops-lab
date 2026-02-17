package bookshop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating cart item quantity
 */
public class UpdateCartItemRequestDto {
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Constructors
    public UpdateCartItemRequestDto() {
    }

    public UpdateCartItemRequestDto(Integer quantity) {
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "UpdateCartItemRequestDto{" +
                "quantity=" + quantity +
                '}';
    }
}
