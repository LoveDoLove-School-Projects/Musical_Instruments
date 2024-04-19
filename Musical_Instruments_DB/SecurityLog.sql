CREATE TABLE SecurityLog (
    pkid INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT NOT NULL,
    role VARCHAR(255) NOT NULL CHECK (role IN ('admin', 'customer')),
    action VARCHAR(255) NOT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255)
);

DROP TABLE SecurityLog;