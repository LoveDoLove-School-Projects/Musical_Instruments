CREATE TABLE customers (
    customer_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10), 
    account_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_date TIMESTAMP
);

CREATE TABLE customer_addresses (
    address_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT,
    address VARCHAR(255),
    city VARCHAR(100),
    state_province VARCHAR(100),
    zip_postal_code VARCHAR(20),
    country VARCHAR(100),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

DROP TABLE customer_addresses;
DROP TABLE customers;