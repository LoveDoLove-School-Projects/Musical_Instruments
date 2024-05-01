CREATE TABLE SecurityLog (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    action VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255)
);

DROP TABLE SecurityLog;