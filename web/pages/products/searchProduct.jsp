<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Products" %>
<%
String IMAGE_DEFAULT_PATH = "assets/database/productImage/";
String searchCategory = request.getParameter("search_category");
List<Products> productDetails = (List<Products>) request.getAttribute("products");
    String imageDefaultPath = "assets/database/productImage/";
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Search Products Page</title>
        <link rel="stylesheet" href="assets/css/product.css" />
    </head>

    <body>
        <style>
            .image{
                width: 100px;
                height: 100px;
            }
            .list{
                border-style: ridge;
                font-family: "Lucida Console", "Courier New", monospace;
                padding: 15px;
                cursor: pointer;
                background-color: azure;
            }
            .list:hover{
                background-color: lightblue;
                transition: 0.5s;
            }

        </style>
        <jsp:include page="/defaults/header.jsp" />
        <main class="main">
            <section class="section1">
                <div class="container-fluid">
                    <div class="row m-2 m-xxl-5 p-0 p-xxl-5 d-flex align-items-center justify-content-center">
                        <div class="col-12 col-xxl-12">
                            <div class="m-5 d-block text-center" style="animation:fadeInUp 5s">
                                <h1>This is our product that we have</h1>
                                <h5>Here have 4 category with various products to let you choose</h5>
                            </div>
                        </div>

                        <!-- Display search bar -->
                        <div class="container">
                            <div class="row justify-content-center">
                                <div class="col-12 col-xxl-6">
                                    <form action="pages/productsearch" method="POST">
                                        <div class="input-group mb-3">
                                            <input type="text" class="form-control" name="searchQuery" id="searchQuery" placeholder="Search by name or category...">
                                            <div class="input-group-append">
                                                <button type="submit" class="btn btn-primary">Search</button>
                                            </div>
                                        </div>
                                        <div id="liveSearchResults"></div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Display search results or all products -->
                        <%
                        List<Products> searchResults = (List<Products>) request.getAttribute("searchResults");
                        if (searchResults != null && !searchResults.isEmpty()) {

                            for (Products product : searchResults) {
                                 String pictureBase64 = Base64.getEncoder().encodeToString(product.getImage());
                                String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                        %>
                        <div class="col-6 col-xl-3 col-xxl-3">
                            <a href="pages/products/viewProduct?product_id=<%=product.getProductId()%>" class="MusicInstruments row1-MusicInstruments1">
                                <div class="pt-4 d-flex justify-content-center">
                                    <img src="<%=imageSrc%>" class="img-fluid w-50 d-flex text-center justify-self-center align-self-center">
                                </div>
                                <p class="text-center"><strong><%=product.getName()%></br>RM<%=product.getPrice()%></strong></p>
                            </a>
                        </div>
                        <%
                            }
                        }
                        %>


                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />

        <script>
            document.getElementById("searchQuery").addEventListener("input", function () {
                let searchQuery = this.value.trim();
                let xhr = new XMLHttpRequest();
                xhr.responseType = 'json';
                xhr.onreadystatechange = function () {
                    if (this.readyState == 4 && this.status == 200) {
                        let jsonResponse = xhr.response;
                        if (jsonResponse.length === 0) {
                            $("#liveSearchResults").html('<div class="alert alert-danger"><strong>No results found!</strong></div>');
                        } else {
                            let result = '';
                            for (let i = 0; i < jsonResponse.length; i++) {
                                let product = jsonResponse[i];
                                let productId = product.productId;
                                let name = product.name;
                                let price = product.price;
                                let color = product.color;
                                let quantity = product.quantity;
                                let category = product.category;
                                let pictureBase64 = arrayBufferToBase64(product.image);
                                let image = 'data:image/png;base64,' + pictureBase64;
                                result += `
                                <div class=list><a href="pages/products/viewProduct?product_id=` + productId + `">&nbsp<img src= ` + image + `  class="image">&nbsp&nbsp&nbsp<b>Name:</b> ` + name + `
                                &nbsp&nbsp&nbsp<b>Category:</b>` + category + `&nbsp&nbsp&nbsp<b>Price:</b>` + price + `</a></div>
                                `;
                            }
                            $("#liveSearchResults").html(result);
                        }
                    }
                };
                xhr.open("POST", "api/products/productsearch?searchQuery=" + searchQuery, true);
                xhr.send();
            });

            function arrayBufferToBase64(buffer) {
                let binary = '';
                let bytes = new Uint8Array(buffer);
                let len = bytes.byteLength;
                for (let i = 0; i < len; i++) {
                    binary += String.fromCharCode(bytes[i]);
                }
                return btoa(binary);
            }
        </script>
    </body>
</html>
