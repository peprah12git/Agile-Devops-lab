package bookshop.models;

import java.math.BigDecimal;

/**
 * Product entity matching database schema
 * Table: products
 * Columns: product_id, name, price, category_id
 */
public class Product {
    
    private int productId;
    private String name;
    private BigDecimal price;
    private int categoryId;
    
    // Optional: Category name (from JOIN with category table)
    private String categoryName;

    // Constructors
    public Product() {
    }

    public Product(String name, BigDecimal price, int categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public Product(int productId, String name, BigDecimal price, int categoryId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                '}';
    }
}