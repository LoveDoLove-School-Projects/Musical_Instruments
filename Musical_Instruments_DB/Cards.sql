CREATE TABLE Cards (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    card_holder_name VARCHAR(100),
    card_number VARCHAR(16),
    exp_year VARCHAR(4),
    exp_month VARCHAR(2),
    cvv VARCHAR(3),
    balance DECIMAL(10, 2)
);

DROP TABLE Cards;