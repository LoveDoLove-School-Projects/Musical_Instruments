CREATE TABLE carts (
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

INSERT INTO carts (customer_id, product_id, product_name, product_quantity, product_color, product_price, product_image_path,subtotal) VALUES (1, 1, 'Guitar', 1, 'Black', 100.00, 'guitar.jpg',100.00);

INSERT INTO carts (customer_id, product_id, product_name, product_quantity, product_color, product_price, product_image_path,subtotal) VALUES (1, 2, 'Piano', 1, 'White', 200.00, 'piano.jpg',200.00);

DROP TABLE carts;