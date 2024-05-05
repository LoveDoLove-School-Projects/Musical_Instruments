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

DROP TABLE Transactions;