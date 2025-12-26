-- Insert seed data for users
-- Note: Passwords are BCrypt hashed versions of 'password123'
INSERT INTO users (username, email, password, first_name, last_name, role, created_at, updated_at) VALUES
('admin', 'admin@codearena.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'System', 'Administrator', 'ADMIN', NOW(), NOW()),
('moderator', 'moderator@codearena.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'John', 'Moderator', 'MODERATOR', NOW(), NOW()),
('testuser', 'testuser@codearena.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test', 'User', 'USER', NOW(), NOW()),
('alice.smith', 'alice.smith@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Alice', 'Smith', 'USER', NOW(), NOW()),
('bob.johnson', 'bob.johnson@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Bob', 'Johnson', 'USER', NOW(), NOW()),
('charlie.brown', 'charlie.brown@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Charlie', 'Brown', 'USER', NOW(), NOW()),
('diana.prince', 'diana.prince@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Diana', 'Prince', 'MODERATOR', NOW(), NOW()),
('eve.adams', 'eve.adams@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Eve', 'Adams', 'USER', NOW(), NOW());
