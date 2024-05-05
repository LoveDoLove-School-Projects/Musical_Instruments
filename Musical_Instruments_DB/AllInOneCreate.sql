CREATE TABLE ArchiveCustomers (
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

CREATE TABLE Carts (
    cart_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL,
    product_color VARCHAR(255) NOT NULL,
    product_price DOUBLE NOT NULL,
    product_image BLOB NOT NULL,
    product_totalprice DOUBLE NOT NULL
);

CREATE TABLE Otps (
    otp VARCHAR(255) PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    try_count INT DEFAULT 0
);

CREATE TABLE Products (
    product_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    color VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    category VARCHAR(255) NOT NULL,
    image BLOB NOT NULL
);

CREATE TABLE ResetPassword (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    token VARCHAR(525) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE SecurityLog (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(50),
    action VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255)
);

CREATE TABLE InternalSecurityLog (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    action VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255)
);

CREATE TABLE Staffs (
    user_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    picture BLOB,
    two_factor_auth BOOLEAN DEFAULT FALSE,
    account_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Transactions (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT NOT NULL,
    transaction_number VARCHAR(255) UNIQUE NOT NULL,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    transaction_status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    date_created_gmt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_updated_gmt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE Cards (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    card_holder_name VARCHAR(100),
    card_number VARCHAR(16),
    expiry_date DATE,
    cvv VARCHAR(3),
    balance DECIMAL(10, 2)
);