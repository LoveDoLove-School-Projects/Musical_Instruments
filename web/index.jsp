<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Musical Instruments</title>
        <style>
            /* Add your CSS styles here */
            body {
                font-family: Arial, sans-serif;
            }
            .container {
                width: 80%;
                margin: 0 auto;
            }
            .header {
                background-color: #333;
                color: #fff;
                padding: 20px;
                text-align: center;
            }
            .product {
                border: 1px solid #ccc;
                margin-bottom: 20px;
                padding: 10px;
                overflow: hidden;
            }
            .product img {
                max-width: 100px;
                float: left;
                margin-right: 10px;
            }
            .product h2 {
                margin-top: 0;
            }
            .product .price {
                color: #009688;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <div class="header">
                <h1>Welcome to our Musical Instruments E-commerce</h1>
            </div>
            <div class="products">
                <div class="product">
                    <img src="guitar.jpg" alt="Guitar" />
                    <h2>Acoustic Guitar</h2>
                    <p class="price">$200</p>
                    <p>Description: Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                    <button>Add to Cart</button>
                </div>
                <div class="product">
                    <img src="piano.jpg" alt="Piano" />
                    <h2>Grand Piano</h2>
                    <p class="price">$1000</p>
                    <p>Description: Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                    <button>Add to Cart</button>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
