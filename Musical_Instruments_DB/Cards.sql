CREATE TABLE Cards (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    card_holder_name VARCHAR(100),
    card_number VARCHAR(16),
    expiry_date DATE,
    cvv VARCHAR(3),
    balance DECIMAL(10, 2)
);

DROP TABLE Cards;