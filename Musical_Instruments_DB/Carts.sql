CREATE TABLE Carts (
    cart_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL,
    product_color VARCHAR(255) NOT NULL,
    product_price DOUBLE NOT NULL,
    product_image BLOB NOT NULL,
    product_totalprice DOUBLE NOT NULL
);

DROP TABLE Carts;