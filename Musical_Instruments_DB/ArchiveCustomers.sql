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
    account_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE ArchiveCustomers;