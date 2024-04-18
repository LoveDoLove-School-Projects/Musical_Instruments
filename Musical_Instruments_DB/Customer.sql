CREATE TABLE customer (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

DROP TABLE customer;