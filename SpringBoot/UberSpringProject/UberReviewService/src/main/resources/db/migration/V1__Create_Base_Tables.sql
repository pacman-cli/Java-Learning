-- V1: Create Base Tables
-- This migration creates all the necessary tables for the Uber Review Service

-- Create driver table
CREATE TABLE driver (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(255),
    licence_number VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255)
);

-- Create passenger table
CREATE TABLE passenger (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(255)
);

-- Create booking table
CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    booking_status ENUM('SCHEDULED', 'CANCELLED', 'CAB_ARRIVED', 'ASSIGNING_DRIVER', 'IN_RIDE', 'COMPLETED'),
    start_time DATETIME,
    end_time DATETIME,
    total_time BIGINT,
    driver_id BIGINT,
    passenger_id BIGINT,
    FOREIGN KEY (driver_id) REFERENCES driver(id),
    FOREIGN KEY (passenger_id) REFERENCES passenger(id)
);

-- Create booking_review table (base Review entity)
CREATE TABLE booking_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    content VARCHAR(255),
    rating DOUBLE,
    booking_id BIGINT UNIQUE,
    FOREIGN KEY (booking_id) REFERENCES booking(id)
);

-- Create passenger_review table (inherits from booking_review)
CREATE TABLE passenger_review (
    id BIGINT PRIMARY KEY,
    passenger_review_content VARCHAR(255) NOT NULL,
    passenger_rating DOUBLE,
    FOREIGN KEY (id) REFERENCES booking_review(id)
);
