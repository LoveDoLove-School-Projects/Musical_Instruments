<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<jsp:useBean id="productDetails" class="entities.Products" scope="session" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Ratings" %>
<%@ page import="entities.Products" %>
<%@ page import="entities.Orders" %>
<%@ page import="jakarta.persistence.EntityManager"%>
<%
String IMAGE_DEFAULT_PATH = "assets/database/productImage/";

%>
<!DOCTYPE html>
<html> 
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Products Page</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <style>

            .addtocartbtn {
                background-color: #d9d9e1;
                border: solid rgb(82, 78, 78) 2px;
                color: black;
                font-size: 20px;
            }

            .addtocartbtn:hover {
                background-color: #343333;
                color: #d9d9e1;
                transition: 0.5s ease;
            }

            .productImage{
                background-color: #d9d9e1;
                border: solid black 0spx;
            }



            .qty-container .input-qty{
                border: 1px solid #d4d4d4;
                height: 38px;
                font-size: 20px;
            }

            .qty-container .qty-btn-plus,
            .qty-container .qty-btn-minus{
                border: 1px solid #d4d4d4;
                font-size: 20px;
                height: 38px;
                width: 38px;
                transition: 0.3s;
            }

            .ratingForm{
                border: solid black 2px;
                border-radius: 20px;
            }

            .ratingResultList{
                border: solid black 2px;
                border-radius: 20px;
            }



        </style>

        <main class="main">
            <section class="section">
                <div class="container">
                    <div class="row m-5 d-flex align-items-center justify-content-center">
                        <%
                            int productId = productDetails.getProductId();
                            String pictureBase64 = Base64.getEncoder().encodeToString(productDetails.getImage());
                            String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                        %>
                        <div class="row m-5 d-flex align-items-center justify-content-center">
                            <div class="col-12 row productPanel">
                                <div class="col-5 p-5 d-flex justify-content-center align-content-centers productImage">
                                    <img src="<%=imageSrc%>" class="img-fluid w-100 m-5">
                                </div>
                                <div class="col-7 px-5">
                                    <h1 class="m-4"><strong><%=productDetails.getName()%></strong></h1>
                                    <hr>
                                    <h3 class="my-4"><strong>RM <%=productDetails.getPrice()%></strong></h3>
                                    <h3 class="my-4"><strong>Stock:</strong> <%=productDetails.getQuantity()%></h3>
                                    <h3 class="my-4"><strong>Color:</strong> <%=productDetails.getColor()%></h3>
                                    <hr>
                                    <h5 class="my-2"><strong>Quantity</strong></h5>
                                    <form method="POST" action="pages/products/addProductToCart" class="qty-container" id="addToCartForm">
                                        <button class="qty-btn-minus" type="button"><i class="fa fa-minus"></i></button>
                                        <input type="number" name="productQuantity" value="1" class="input-qty w-50 text-center p-2" min="1" max="<%=productDetails.getQuantity()%>"/>
                                        <input type="hidden" name="productId" value="<%=productId%>"/>
                                        <button class="qty-btn-plus" type="button"><i class="fa fa-plus"></i></button><br>
                                        <button type="submit" class="my-4 mx-auto p-2 addtocartbtn"><strong>Add to cart</strong></button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%
      EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
      List<Orders> orders = (List<Orders>) request.getAttribute("orderList");
                     
      try {
          for (Orders order : orders) {
              if (order.getProductId() == productId) {
                    %>
                    <div class="row  d-flex align-items-center justify-content-center">
                        <div class="col-12 ratingForm w-50">
                            <h2 class="text-center">Rate this product:</h2>
                            <form action="pages/ratings/addRating" method="post" class="w-50 mx-auto">
                                <input type="hidden" name="product_id" value="<%=productId%>" />
                                <label for="rating" class="w-50">Rating:</label>
                                <select name="rating" id="rating" class="w-100">
                                    <!-- 5 stars so 5 options -->
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                                <br>
                                <label for="comment" class="w-25">Comment:</label>
                                <textarea name="comment" id="comment" class="w-100"></textarea>
                                <br>
                                <div class="text-center">
                                    <button type="submit" class="addtocartbtn m-3 p-1"><strong>Submit</strong></button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <%
                                    break; // Exit the loop once a matching order is found
                                }
                            }
                        } catch (Exception e) {
                            // Handle the exception
                            e.printStackTrace();
                        }
                    %>




                    <h1 class="text-center">Product Ratings</h1>
                    <div class="ratingResultList">
                        <%
                            List<Ratings> ratings = (List<Ratings>) request.getAttribute("ratingList");
                             if (!ratings.isEmpty()&&ratings!=null) {
                                for (Ratings rating : ratings) {
                        %>
                        <div class="rating m-3">
                            <p><strong>Rating:</strong> <%= rating.getRatingScore() %></p>
                            <p><strong>Comment:</strong> <%= rating.getComment() %></p>
                            <p><strong>Timestamp:</strong> <%= rating.getTimestamp() %></p>
                            <hr>
                        </div>
                        <%
                                }
                            } else {
                        %>
                        <p class="text-center m-5"><strong>No ratings available for this product.</strong></p>
                        <%
                            }
                        %>
                    </div>

                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/carts.js"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const stars = document.querySelectorAll('.star');
                stars.forEach(function (star) {
                    star.addEventListener('click', function () {
                        let rating = this.getAttribute('data-rating');
                        let productId = this.getAttribute('data-product-id');
                        // Send rating and product ID to server
                        sendRatingToServer(productId, rating);
                        // Change star color immediately (optional)
                        applyStarColor(star, rating);
                    });
                });
            });

// Function to send rating to the server
            function sendRatingToServer(productId, rating) {
                // Fetch API (This one from youtube I also dont know the exact way)
                fetch('rating', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({productId: productId, rating: rating}),
                })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log('Rating sent successfully:', data);
                            // Handle server response if needed
                        })
                        .catch(error => {
                            console.error('Error sending rating:', error);
                            // Handle error
                        });
            }

// Function to change star color immediately
            function applyStarColor(star, rating) {
                // Example: change color of clicked star and stars before it
                stars.forEach(function (s) {
                    if (parseInt(s.getAttribute('data-rating')) <= parseInt(rating)) {
                        s.style.color = 'yellow';
                    } else {
                        s.style.color = 'grey';
                    }
                });
            }

        </script>
    </body>
</html>
