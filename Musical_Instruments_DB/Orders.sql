CREATE TABLE Orders (
    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_quantity INT NOT NULL,
    product_color VARCHAR(255) NOT NULL,
    product_price DOUBLE NOT NULL,
    product_image BLOB NOT NULL,
    product_totalprice DECIMAL(10, 2) NOT NULL,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
);


verify status
if true
read all entity carts with user id using session checker
read all product with product id in the carts product id then use cart product id to deduct the product quantity
