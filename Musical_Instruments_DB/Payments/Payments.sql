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

-- Insert sample data into BankUsers table
INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jun Xiang', 'Chong', 'chongjx-wm22@student.tarc.edu.my', 'chongjx-wm22', 'Chong Jun Xiang', '5555444433332222', '2025-12-31', '123', 1000.00, 'MYR'); -- Mastercard
INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jia Jie', 'Tan', 'jjtan-wm22@student.tarc.edu.my', 'jjtan-wm22', 'Tan Jia Jie', '4444333322221111', '2026-12-31', '123', 2000.00, 'MYR'); -- Visa

-- Insert sample data into Transactions table
INSERT INTO Transactions (user_id, transaction_number, order_number, transaction_status, payment_method, currency, total_amount)
VALUES (1, '123456', '123456', 'completed', 'credit_card', 'MYR', 100.00);

DROP TABLE BankUsers;
DROP TABLE Transactions;