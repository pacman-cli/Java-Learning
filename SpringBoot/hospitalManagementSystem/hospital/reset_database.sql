-- Database Reset Script for Hospital Management System
-- This script completely resets the database to allow Flyway migrations to run cleanly
-- WARNING: This will delete ALL data in the database!
-- Use this only in development environment

-- Drop the database if it exists
DROP DATABASE IF EXISTS hospital_db;

-- Create a fresh database
CREATE DATABASE hospital_db;

-- Use the database
USE hospital_db;

-- The database is now empty and ready for Flyway migrations
-- Run your Spring Boot application to let Flyway create all tables
