INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jun Xiang', 'Chong', 'chongjx-wm22@student.tarc.edu.my', 'chongjx-wm22', 'Chong Jun Xiang', '5555444433332222', '2025-12-31', '123', 1000.00, 'MYR'); -- Mastercard
INSERT INTO BankUsers (FirstName, LastName, Email, Password, Card_Holder_Name, Card_Number, Expiry_Date, Balance, Currency)
VALUES ('Jia Jie', 'Tan', 'jjtan-wm22@student.tarc.edu.my', 'jjtan-wm22', 'Tan Jia Jie', '4444333322221111', '2026-12-31', '123', 2000.00, 'MYR'); -- Visa

INSERT INTO Transactions (user_id, transaction_number, order_number, transaction_status, payment_method, currency, total_amount)
VALUES (1, '123456', '123456', 'completed', 'credit_card', 'MYR', 100.00);

INSERT INTO Staffs (username, password, email, address, phone_number, gender)
VALUES ('Chong Jun Xiang', 'qqWWEDYgqA7UkvID9vyLSQ==', 'chongjx-wm22@student.tarc.edu.my', 'test', '0123456789', 'Male');