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
                                            <input type="text" class="form-control" name="searchQuery" placeholder="Search...">
                                            <div class="input-group-append">
                                                <button type="submit" class="btn btn-primary">Search</button>
                                            </div>
                                        </div>
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
                                <p class="text-center"><strong><%=product.getName()%></br>RM<%=product.getQuantity()%></strong></p>
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
    </body>
</html>
