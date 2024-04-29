CREATE TABLE products (
    product_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    color VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    category VARCHAR(255) NOT NULL,
    image_path VARCHAR(255) NOT NULL
);


DROP TABLE products;