-- Create a new database
CREATE DATABASE prod_db;

-- Connect to the new database
\c prod_db;

-- Create a sample table
CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    original_url VARCHAR(255) NOT NULL,
    short_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_date TIMESTAMP,
    access_count INT DEFAULT 0
);