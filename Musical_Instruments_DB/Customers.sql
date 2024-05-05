CREATE TABLE Customers (
    user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    picture BLOB,
    two_factor_auth BOOLEAN DEFAULT FALSE,
    -- Billing details
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    country VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(10),
    account_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE Customers;