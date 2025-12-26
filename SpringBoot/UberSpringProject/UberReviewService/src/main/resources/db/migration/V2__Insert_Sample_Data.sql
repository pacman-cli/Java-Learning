-- V2: Insert Sample Data
-- This migration adds sample data for testing the application

-- Insert sample drivers
INSERT INTO driver (name, licence_number, phone_number) VALUES
('John Smith', 'DL001234', '+1234567890'),
('Sarah Johnson', 'DL005678', '+1234567891'),
('Mike Wilson', 'DL009012', '+1234567892');

-- Insert sample passengers
INSERT INTO passenger (name) VALUES
('Alice Brown'),
('Bob Davis'),
('Carol Evans');

-- Insert sample bookings
INSERT INTO booking (booking_status, start_time, end_time, total_time, driver_id, passenger_id) VALUES
('COMPLETED', '2024-01-15 10:00:00', '2024-01-15 10:30:00', 30, 1, 1),
('COMPLETED', '2024-01-15 14:00:00', '2024-01-15 14:45:00', 45, 2, 2),
('IN_RIDE', '2024-01-15 16:00:00', NULL, NULL, 3, 3);

-- Insert sample reviews (base Review entities)
INSERT INTO booking_review (content, rating, booking_id) VALUES
('Great driver, very professional and on time!', 5.0, 1),
('Good ride but a bit late', 4.0, 2);

-- Insert sample passenger reviews (inheriting from booking_review)
INSERT INTO passenger_review (id, passenger_review_content, passenger_rating) VALUES
(1, 'Passenger was very polite and clean', 5.0),
(2, 'Passenger was a bit rude', 3.0);
