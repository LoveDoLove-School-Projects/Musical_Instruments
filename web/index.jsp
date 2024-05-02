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
        <main class="">
            <section class="section1">
                <div class="container">
                    <div class="row justify-content-center align-items-center">
                        <div class="d-block my-auto p-5 col-12 col-xl-5 sec1Content" style="animation:swing 1s">
                            <h1>Welcome to music instruments</h1>
                            <p>Enjoy the music, Malaysia top 1 music website</p>
                            <a href="pages/products">
                            <button class="m-2 px-4 py-2 button"><strong>Shop now</strong></button>
                            </a>
                        </div>

                        <div class="col-12 col-xl-7">
                            <div class="m-3 mx-auto" style="animation:bounceInRight 2s">
                                <img src="">
                            </div>
                        </div>
                    </div>
            </section>


            <section class="section2">
                <div class="container-fluid">
                    <div class="row justify-content-center align-items-center">
                        <div class="m-5 col-12 col-xl-6 col-xxl-5">
                            <div class="d-block p-5  sec2_content" style="animation:bounce 2s">
                                <h1>Want to be member ?</h1><br>
                                <p>With our state-of-the-art content management and delivery system placing content orders has never been faster and easier. You can order content in multiple languages in just few clicks. Furthermore, our professional content writers are ready to finalise your request as fast as possible.</p>
                            </div>
                        </div>
                        <div class="col-12 col-xl-6 col-xxl-5 d-flex align-content-center justify-content-center">
                            <div class="m-5 p-5 d-block text-center PremiumBox">
                                <h1>Premium</h1>
                                <ul>
                                    <li>Offer coupons and discounts</li>
                                    <li>Birthday reward</li>
                                    <li>Free some piano</li>
                                    <li>online community</li>
                                </ul>
                                <a href="pages/login"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Login</strong></button></a>
                                <a href="pages/register"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Sign up</strong></button></a>
                            </div>
                        </div>
                    </div>
                </div>
            </section>


            <div class="container mt-5">
                <h2>Featured Products</h2>
                <!-- ${topsales} -->
                <div class="row">
                    <div class="col-md-3">
                        <div class="card">
                            <img src="assets/image/logo.png" class="img-fluid"  height="300" alt="...">
                            <div class="card-body">
                                <h5 class="card-title">Guitar</h5>

                                <a href="pages/products" class="btn btn-primary">Buy Now</a>
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
        </main>

        <jsp:include page="/defaults/footer.jsp" />
    </body>

</html>