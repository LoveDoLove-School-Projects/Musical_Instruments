<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.*" %>
<%@ page import="entities.Products" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <meta charset="UTF-8">
        <title>Search Products</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
            body, html {
                margin: 0;
                padding: 0;
            }
            body {
                min-height: 100vh;
                background-color: #fbfbfb;
                font-family: 'Roboto', sans-serif;
            }

            .btn {
                width: 100%;
                padding: 15px;
                border: groove 10px black;
                background-color: grey;
                color: #fff;
                border-radius: 5px;
                transition: all 0.3s ease;
                font-size: 22px;
                font-weight: bold;
                text-decoration: none;
                justify-content: center;
                align-content: center;
            }
            .btn:hover {
                background-color: lightslategray;
            }
            .product-search-box {
                background-color: #996600;
                color: #fff;
                border: 10px double #d2c20b;
                border-radius: 10px;
                padding: 20px;
                text-align: center;
                margin-bottom: 20px;
            }
            .product-search-box i {
                font-size: 36px;
                margin-bottom: 10px;
            }

        </style>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <br><h1><i class="fas fa-box">  Product Management</i></h1><br>
            <div class="product-search-box">
                <i class="fas fa-box"></i>
            </div>
            <form class="form-inline mb-3" action="pages/staffs/staffSearchProduct" method="POST">
                <input class="col-5 form-control mr-2" type="text" name="searchQuery" placeholder="Enter product ID: "><br>
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="pages/staffs/addProduct" class="btn btn-success">+ <i class="fas fa-box"></i>  Add New Product</a>
            </form>
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-default">
                            <div class="panel-body table-responsive">
                                <table id="productTable" class="table table-striped table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Image</th>
                                            <th>Name</th>
                                            <th>Price</th>
                                            <th>Quantity</th>
                                            <th>Color</th>
                                            <th>Category</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            List<Products> productList = (List<Products>) request.getAttribute("productList");
                                            if (productList != null) {
                                                for (Products product : productList) {
                                                    String pictureBase64 = Base64.getEncoder().encodeToString(product.getImage());
                                                    String imageSrc = "data:image/png;base64," + pictureBase64;
                                        %>
                                        <tr>
                                            <td><%=product.getProductId()%></td>
                                            <td>
                                                <img src="<%=imageSrc%>" alt="<%=product.getName()%>" class="img-fluid center" loading="lazy" style="width: 75px; height: 75px; object-fit: cover;"/>
                                            </td>
                                            <td><%=product.getName()%></td>
                                            <td><%=product.getPrice()%></td>
                                            <td><%=product.getQuantity()%></td>
                                            <td><%=product.getColor()%></td>
                                            <td><%=product.getCategory()%></td>
                                        </tr>
                                        <% }
                                    } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module">
            $(document).ready(function () {
                $('#productTable').DataTable();
            });
        </script>
    </body>
</html>
