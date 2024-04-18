CREATE TABLE admin (
    user_id INT PRIMARY KEY,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

INSERT INTO admin (user_id) VALUES (1);

DROP TABLE admin;