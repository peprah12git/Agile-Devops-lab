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
    course VARCHAR(100),
    age INT CHECK (age >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================
-- Indexes for Performance
-- ==========================
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_inventory_product ON inventory(product_id);
CREATE INDEX idx_products_name ON products(name);

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

-- Insert sample users
INSERT INTO users (name, email, course, age) VALUES
    ('John Doe', 'john@example.com', 'Computer Science', 22),
    ('Jane Smith', 'jane@example.com', 'Mathematics', 21),
    ('Bob Johnson', 'bob@example.com', 'Physics', 23);