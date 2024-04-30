<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="utilities.FileUtilities"%>
<%@ page import="entities.Products" %>
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
                border: solid rgb(82, 78, 78) 1px;
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

        </style>

        <main class="main">
            <section class="section">
                <div class="container">
                    <div class="row m-5 d-flex align-items-center justify-content-center">
                        <%
                            Products product = (Products) request.getAttribute("productDetails");
                            int productId = product.getProductId();
                            String pictureBase64 = Base64.getEncoder().encodeToString(product.getImage());
                            String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                        %>
                        <div class="row m-5 d-flex align-items-center justify-content-center">
                            <div class="col-12 row productPanel">
                                <div class="col-5 p-5 d-flex justify-content-center align-content-centers productImage">
                                    <img src="<%=imageSrc%>" class="img-fluid w-100 m-5">
                                </div>
                                <div class="col-7 px-5">
                                    <h1 class="m-4"><strong><%=product.getName()%></strong></h1>
                                    <hr>
                                    <h3 class="my-4"><strong>RM <%=product.getPrice()%></strong></h3>
                                    <h3 class="my-4"><strong>Stock:</strong> <%=product.getQuantity()%></h3>
                                    <h3 class="my-4"><strong>Color:</strong> <%=product.getColor()%></h3>
                                    <hr>
                                    <h5 class="my-2"><strong>Quantity</strong></h5>
                                    <form method="POST" action="pages/products/addProductToCart" class="qty-container">
                                        <button class="qty-btn-minus" type="button"><i class="fa fa-minus"></i></button>
                                        <input type="number" name="productQuantity" value="1" class="input-qty w-50 text-center p-2" min="1" max="<%=product.getQuantity()%>"/>
                                        <input type="hidden" name="productId" value="<%=productId%>"/>
                                        <button class="qty-btn-plus" type="button"><i class="fa fa-plus"></i></button><br>
                                        <button type="submit" class="my-4 mx-auto p-2 addtocartbtn"><strong>Add to cart</strong></button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/carts.js"></script>
    </body>
</html>
