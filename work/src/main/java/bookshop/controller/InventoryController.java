package bookshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bookshop.dto.request.InventoryCreateDto;
import bookshop.dto.request.StockUpdateDto;
import bookshop.dto.response.InventoryResponseDto;
import bookshop.exceptions.InventoryNotFoundException;
import bookshop.exceptions.ProductNotFoundException;
import bookshop.models.Inventory;
import bookshop.services.serviceInterface.InventoryService;
import bookshop.services.serviceInterface.ProductService;
import jakarta.validation.Valid;

/**
 * REST Controller for Inventory operations
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;
    private final ProductService productService;
    
    @Autowired
    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    /**
     * Create a new inventory record for a product
     * 
     * POST /api/inventory
     * @valid- Triggers automatic validation of the DTO(checks constraints like @NotNull @Main
     * 
     * @param inventoryCreateDto the inventory data to create
     * @return the created inventory with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<InventoryResponseDto> createInventory(@Valid @RequestBody InventoryCreateDto inventoryCreateDto) {
        // Verify product exists
        productService.getProductById(inventoryCreateDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + inventoryCreateDto.getProductId()));

        // Create inventory
        Inventory inventory = new Inventory();
        inventory.setProductId(inventoryCreateDto.getProductId());
        inventory.setQuantity(inventoryCreateDto.getQuantityAvailable());

        Inventory createdInventory = inventoryService.createInventory(inventory);
        
        // Build response
        InventoryResponseDto response = buildInventoryResponse(createdInventory);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get inventory information for a specific product
     * 
     * GET /api/inventory/{productId}
     * 
     * @param productId the product ID
     * @return the inventory information
     */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> getInventoryByProductId(@PathVariable int productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product ID: " + productId));
        
        InventoryResponseDto response = buildInventoryResponse(inventory);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Update the quantity for a product
     * 
     * PUT /api/inventory/{productId}/quantity
     * 
     * @param productId the product ID
     * @param stockUpdateDto the new quantity
     * @return the updated inventory
     */
    @PutMapping("/{productId}/quantity")
    public ResponseEntity<InventoryResponseDto> updateQuantity(
            @PathVariable int productId,
            @Valid @RequestBody StockUpdateDto stockUpdateDto) {
        
        Inventory updatedInventory = inventoryService.updateQuantity(productId, stockUpdateDto.getQuantity());
        InventoryResponseDto response = buildInventoryResponse(updatedInventory);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Add stock to existing inventory
     * 
     * POST /api/inventory/{productId}/add-stock
     * 
     * @param productId the product ID
     * @param stockUpdateDto the quantity to add
     * @return the updated inventory
     */
    @PostMapping("/{productId}/add-stock")
    public ResponseEntity<InventoryResponseDto> addStock(
            @PathVariable int productId,
            @Valid @RequestBody StockUpdateDto stockUpdateDto) {
        
        Inventory updatedInventory = inventoryService.addStock(productId, stockUpdateDto.getQuantity());
        InventoryResponseDto response = buildInventoryResponse(updatedInventory);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Reduce stock from existing inventory
     * 
     * POST /api/inventory/{productId}/reduce-stock
     * 
     * @param productId the product ID
     * @param stockUpdateDto the quantity to reduce
     * @return the updated inventory
     */
    @PostMapping("/{productId}/reduce-stock")
    public ResponseEntity<InventoryResponseDto> reduceStock(
            @PathVariable int productId,
            @Valid @RequestBody StockUpdateDto stockUpdateDto) {
        
        Inventory updatedInventory = inventoryService.reduceStock(productId, stockUpdateDto.getQuantity());
        InventoryResponseDto response = buildInventoryResponse(updatedInventory);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get available quantity for a product
     * 
     * GET /api/inventory/{productId}/available
     * 
     * @param productId the product ID
     * @return the available quantity
     */
    @GetMapping("/{productId}/available")
    public ResponseEntity<Integer> getAvailableQuantity(@PathVariable int productId) {
        int availableQuantity = inventoryService.getAvailableQuantity(productId);
        return ResponseEntity.ok(availableQuantity);
    }

    /**
     * Check if a product is in stock
     * 
     * GET /api/inventory/{productId}/in-stock
     * 
     * @param productId the product ID
     * @return true if product has stock > 0, false otherwise
     */
    @GetMapping("/{productId}/in-stock")
    public ResponseEntity<Boolean> isInStock(@PathVariable int productId) {
        boolean inStock = inventoryService.isInStock(productId);
        return ResponseEntity.ok(inStock);
    }

    /**
     * Check if a product has sufficient stock
     * 
     * GET /api/inventory/{productId}/check-stock?quantity=5
     * 
     * @param productId the product ID
     * @param quantity the required quantity
     * @return true if sufficient stock available, false otherwise
     */
    @GetMapping("/{productId}/check-stock")
    public ResponseEntity<Boolean> hasStock(
            @PathVariable int productId,
            @RequestParam int quantity) {
        
        boolean hasSufficientStock = inventoryService.hasStock(productId, quantity);
        return ResponseEntity.ok(hasSufficientStock);
    }

    /**
     * Delete inventory record for a product
     * 
     * DELETE /api/inventory/{productId}
     * 
     * @param productId the product ID
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable int productId) {
        inventoryService.deleteInventory(productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to build InventoryResponseDto from Inventory model
     * 
     * @param inventory the inventory model
     * @return inventory response DTO
     */
    private InventoryResponseDto buildInventoryResponse(Inventory inventory) {
        String productName = productService.getProductById(inventory.getProductId())
                .map(product -> product.getName())
                .orElse("Unknown Product");
        
        boolean inStock = inventory.getQuantity() > 0;
        
        return new InventoryResponseDto(
                inventory.getInventoryId(),
                inventory.getProductId(),
                productName,
                inventory.getQuantity(),
                inventory.getLastUpdated(),
                inStock
        );
    }
}
