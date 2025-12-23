-- Sample Data for Product Pagination API Testing
-- This script inserts sample products into the database for testing pagination and sorting

-- Clear existing data (optional - uncomment if needed)
-- TRUNCATE TABLE products RESTART IDENTITY CASCADE;
\c pagination_db;
\dt;

-- Insert sample products with various prices and names for testing
INSERT INTO products (name, description, price)
VALUES ('Laptop', 'High-performance laptop with 16GB RAM and 512GB SSD', 999.99),
       ('Wireless Mouse', 'Ergonomic wireless optical mouse with USB receiver', 25.50),
       ('Mechanical Keyboard', 'RGB mechanical keyboard with Cherry MX switches', 89.99),
       ('27-inch Monitor', '4K UHD display with HDR support and 144Hz refresh rate', 399.00),
       ('Noise-Cancelling Headphones', 'Premium wireless headphones with active noise cancellation', 249.99),
       ('USB-C Hub', '7-in-1 USB-C hub with HDMI, USB 3.0, and card reader', 45.99),
       ('Webcam', '1080p HD webcam with built-in microphone', 69.99),
       ('External SSD', '1TB portable SSD with USB 3.2 Gen 2 interface', 129.99),
       ('Desk Lamp', 'LED desk lamp with adjustable brightness and color temperature', 34.99),
       ('Laptop Stand', 'Aluminum laptop stand with adjustable height', 39.99),
       ('Cable Organizer', 'Cable management box for desk organization', 19.99),
       ('Screen Protector', 'Tempered glass screen protector for 15-inch laptops', 15.99),
       ('Laptop Bag', 'Water-resistant laptop backpack with multiple compartments', 59.99),
       ('Bluetooth Speaker', 'Portable Bluetooth speaker with 360-degree sound', 79.99),
       ('Phone Stand', 'Adjustable phone stand for desk use', 12.99),
       ('Microphone', 'USB condenser microphone for podcasting and streaming', 89.99),
       ('Drawing Tablet', 'Graphics tablet with 8192 pressure levels', 199.99),
       ('Portable Charger', '20000mAh power bank with fast charging', 39.99),
       ('Smart Watch', 'Fitness tracker with heart rate monitor', 149.99),
       ('Wireless Earbuds', 'True wireless earbuds with charging case', 79.99),
       ('HDMI Cable', '4K HDMI 2.1 cable 6 feet', 14.99),
       ('USB Flash Drive', '128GB USB 3.0 flash drive', 22.99),
       ('Router', 'WiFi 6 dual-band wireless router', 119.99),
       ('Gaming Mouse', 'Programmable gaming mouse with RGB lighting', 49.99),
       ('Mousepad', 'Extended gaming mousepad with non-slip base', 24.99);

-- Verify the data was inserted
SELECT COUNT(*) as total_products
FROM products;

-- Show sample of inserted data
SELECT *
FROM products
ORDER BY id
LIMIT 10;

-- Query examples for testing pagination
-- These are comments showing example queries you can run:

-- Get first page (5 items):
-- SELECT * FROM products ORDER BY name LIMIT 5 OFFSET 0;

-- Get second page (5 items):
-- SELECT * FROM products ORDER BY name LIMIT 5 OFFSET 5;

-- Sort by price (ascending):
-- SELECT * FROM products ORDER BY price ASC;

-- Sort by price (descending):
-- SELECT * FROM products ORDER BY price DESC;

-- Count products in different price ranges:
-- SELECT
--     COUNT(CASE WHEN price < 50 THEN 1 END) as budget,
--     COUNT(CASE WHEN price BETWEEN 50 AND 150 THEN 1 END) as mid_range,
--     COUNT(CASE WHEN price > 150 THEN 1 END) as premium
-- FROM products;
