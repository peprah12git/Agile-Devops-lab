-- ==========================
-- Complete Bookshop Database Schema
-- ==========================

-- Create Database
CREATE DATABASE IF NOT EXISTS bookshop;

-- Use the database
USE bookshop;

-- ==========================
-- Category Table
-- ==========================
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- ==========================
-- Products Table
-- ==========================
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category_id INT NOT NULL,

    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================
-- Inventory Table
-- ==========================
CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT unique_product_inventory
        UNIQUE (product_id),
    
    CONSTRAINT check_quantity_non_negative
        CHECK (quantity >= 0)
);

-- ==========================
-- Users Table
-- ==========================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    course VARCHAR(100),
    age INT CHECK (age >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT check_role 
        CHECK (role IN ('CUSTOMER', 'STAFF', 'ADMIN'))
);

-- ==========================
-- Shopping Cart Table
-- ==========================
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT unique_user_cart
        UNIQUE (user_id)
);

-- ==========================
-- Cart Items Table
-- ==========================
CREATE TABLE cart_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id)
        REFERENCES cart(cart_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_cart_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    CONSTRAINT check_cart_quantity_positive
        CHECK (quantity > 0),
    
    CONSTRAINT unique_cart_product
        UNIQUE (cart_id, product_id)
);

-- ==========================
-- Indexes for Performance
-- ==========================
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_inventory_product ON inventory(product_id);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_cart_user ON cart(user_id);
CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);

-- ==========================
-- Sample Data (Optional)
-- ==========================

-- Insert sample categories
INSERT INTO category (name) VALUES 
    ('Fiction'),
    ('Non-Fiction'),
    ('Science'),
    ('Technology'),
    ('History');

-- Insert sample products
INSERT INTO products (name, price, category_id) VALUES
    ('The Great Gatsby', 15.99, 1),
    ('1984', 12.99, 1),
    ('Sapiens', 18.99, 2),
    ('Clean Code', 45.99, 4),
    ('A Brief History of Time', 22.99, 3);

-- Insert sample inventory
INSERT INTO inventory (product_id, quantity) VALUES
    (1, 50),
    (2, 30),
    (3, 25),
    (4, 15),
    (5, 40);

-- Insert sample users (password is "password123" for all users - hashed with BCrypt)
INSERT INTO users (name, email, password, role, course, age) VALUES
    ('John Doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Computer Science', 22),
    ('Jane Smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 'Mathematics', 21),
    ('Bob Johnson', 'bob@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'Physics', 23);