<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Musical Instruments</title>
        <link rel="stylesheet" href="assets/css/main.css"/>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
        <!-- Hero Section -->
        <div class="container mainImage mt-6">
            <div class="row">
                <div class="col-md-12 section1">
                    <div class="content m-5">
                        <h1>Welcome to Our Music Store</h1>
                        <p class="lead">Find your favorite musical instruments here.</p>
                        <a href="product.jsp" class="btn btn-primary">Shop Now</a>
                    </div>
                </div>
            </div>
        </div>


        <!-- Featured Products -->
        <div class="container mt-5">
            <h2>Featured Products</h2>
            <!-- ${topsales} -->
            <div class="row">
                <div class="col-md-3">
                    <div class="card">
                        <img src="assets/image/logo.png" class="img-fluid"  height="300" alt="...">
                        <div class="card-body">
                            <h5 class="card-title">Guitar</h5>

                            <a href="product.jsp" class="btn btn-primary">Buy Now</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <img src="drum.jpg" class=" card-img-top " height="300" alt="...">
                        <div class="card-body">
                            <h5 class="card-title">Drum</h5>

                            <a href="#" class="btn btn-primary">Buy Now</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card">
                        <img src="piano.jpg" class="card-img-top" height="300" alt="...">
                        <div class="card-body">
                            <h5 class="card-title">Piano</h5>

                            <a href="#" class="btn btn-primary">Buy Now</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/defaults/footer.jsp" />
    </body>

</html>