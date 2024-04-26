CREATE TABLE BankUsers (
    UserID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    FirstName VARCHAR(100),
    LastName VARCHAR(100),
    Email VARCHAR(255),
    Password VARCHAR(255),
    Card_Holder_Name VARCHAR(100),
    Card_Number VARCHAR(16),
    Expiry_Date DATE,
    CVV VARCHAR(3),
    Balance DECIMAL(10, 2),
    Currency VARCHAR(3)
);

-- Create Transactions table
CREATE TABLE Transactions (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    status INT NOT NULL,
    currency VARCHAR(10) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    customer_id INT NOT NULL,
    date_created_gmt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_updated_gmt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    parent_order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(50) NOT NULL,
    ip_address VARCHAR(50) NOT NULL,
    user_agent VARCHAR(500) NOT NULL,
    customer_note VARCHAR(500) NOT NULL
);

-- Insert sample data into BankUsers table
INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jun Xiang', 'Chong', 'chongjx-wm22@student.tarc.edu.my', 'chongjx-wm22', 'Chong Jun Xiang', '5555444433332222', '2025-12-31', '123', 1000.00, 'MYR'); -- Mastercard
INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jia Jie', 'Tan', 'jjtan-wm22@student.tarc.edu.my', 'jjtan-wm22', 'Tan Jia Jie', '4444333322221111', '2026-12-31', '123', 2000.00, 'MYR'); -- Visa

-- Insert sample data into Transactions table
INSERT INTO Transactions (status, currency, total_amount, customer_id, parent_order_id, payment_method, transaction_id, ip_address, user_agent, customer_note)
VALUES (1, 'MYR', 100.00, 1, 1, 'Mastercard', '123456', '0.0.0.0', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3', 'Deposit to account');

DROP TABLE BankUsers;
DROP TABLE Transactions;