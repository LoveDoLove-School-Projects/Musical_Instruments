-- Create BankUsers table
CREATE TABLE BankUsers (
    UserID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    FirstName VARCHAR(100),
    LastName VARCHAR(100),
    Email VARCHAR(255),
    Password VARCHAR(255)
);

-- Create BankAccounts table
CREATE TABLE BankAccounts (
    AccountID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    UserID INT NOT NULL,
    Card_Holder_Name VARCHAR(100),
    Card_Number VARCHAR(16),
    Expiry_Date DATE,
    Balance DECIMAL(10, 2),
    Currency VARCHAR(3),
    FOREIGN KEY (UserID) REFERENCES BankUsers(UserID)
);

-- Create TransactionTypes table
CREATE TABLE TransactionTypes (
    TransactionTypeID INT PRIMARY KEY,
    TransactionTypeName VARCHAR(50)
);

CREATE TABLE TransactionStatus (
    StatusID INT PRIMARY KEY,
    StatusName VARCHAR(50)
);

-- Create Transactions table
CREATE TABLE Transactions (
    TransactionID VARCHAR(50) PRIMARY KEY NOT NULL,
    AccountID INT NOT NULL,
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    Currency VARCHAR(3) NOT NULL,
    TransactionTypeID INT NOT NULL,
    StatusID INT NOT NULL,
    Description VARCHAR(255),
    FOREIGN KEY (AccountID) REFERENCES BankAccounts(AccountID),
    FOREIGN KEY (TransactionTypeID) REFERENCES TransactionTypes(TransactionTypeID),
    FOREIGN KEY (StatusID) REFERENCES TransactionStatus(StatusID)
);

-- Insert sample data into BankUsers table
INSERT INTO BankUsers (FirstName, LastName, Email, Password) VALUES ('Jun Xiang', 'Chong', 'chongjx-wm22@student.tarc.edu.my', 'chongjx-wm22');
INSERT INTO BankUsers (FirstName, LastName, Email, Password) VALUES ('Jia Jie', 'Tan', 'jjtan-wm22@student.tarc.edu.my', 'jjtan-wm22');

-- Insert sample data into BankAccounts table
INSERT INTO BankAccounts (UserID, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES (1, 'Chong Jun Xiang', '5555444433332222', '2025-12-31', 1000.00, 'MYR'); -- Mastercard
INSERT INTO BankAccounts (UserID, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES (2, 'Tan Jia Jie', '4444333322221111', '2026-12-31', 2000.00, 'MYR'); -- Visa

-- Insert sample data into TransactionTypes table
INSERT INTO TransactionTypes VALUES (1, 'DEPOSIT');
INSERT INTO TransactionTypes VALUES (2, 'WITHDRAWAL');
INSERT INTO TransactionTypes VALUES (3, 'TRANSFER');
INSERT INTO TransactionTypes VALUES (4, 'PAYMENT');
INSERT INTO TransactionTypes VALUES (5, 'REFUND');

-- Insert sample data into TransactionStatus table
INSERT INTO TransactionStatus VALUES (0, 'NOT_DONE');
INSERT INTO TransactionStatus VALUES (1, 'APPROVED');
INSERT INTO TransactionStatus VALUES (2, 'DECLINED');
INSERT INTO TransactionStatus VALUES (3, 'CANCELLED');
INSERT INTO TransactionStatus VALUES (4, 'USER_CANCELLED');

-- Insert sample data into Transactions table
INSERT INTO Transactions (TransactionID, AccountID, Amount, Currency, TransactionTypeID, StatusID, Description)
VALUES ('MS20240411012647', 1, 100.00, 'MYR', 1, 1, 'Deposit RM100.00');