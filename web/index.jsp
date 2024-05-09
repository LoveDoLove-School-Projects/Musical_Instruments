<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Sales"%>
<%@ page import="dao.IndexDao"%>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title><%=application.getInitParameter("companyName")%></title>
        <link rel="stylesheet" href="assets/css/index.css"/>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />

        <main class="">
            <section class="section1">
                <div class="container">
                    <div class="row justify-content-center align-items-center ">
                        <div class="d-block my-auto p-5 col-12 col-xl-5 sec1Content" style="animation:swing 1s">
                            <h1>Welcome to <strong><%=application.getInitParameter("companyName")%></strong> Official Website</h1>
                            <p>Enjoy the music, Malaysia Top 1 Music Website</p>
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
                            <div class="d-block p-5  sec2_content " style="animation:bounce 2s">
                                <h1 class="who">WHO WE ARE</h1><br>
                                <p>In the heart of our shared passion for music, five friends joined forces to realize a dream.
                                    From late-night jam sessions to endless discussions over coffee, we birthed our musical emporium.
                                    Established in 2024, our shop offers a curated selection of instruments and accessories.
                                    Come, explore our products, where every chord beckons a symphony of possibilities.</p>
                            </div>
                        </div>
                        <div class="col-12 col-xl-6 col-xxl-5 d-flex align-content-center justify-content-center">
                            <div class="m-5 p-5 d-block text-center PremiumBox">
                                <h1>Get Started!</h1><br>
                                <ul>
                                    <li>Start shopping to access our wide range of products </li>
                                    <li>Stay updated on the latest news and product releases</li>
                                </ul>
                                <a href="pages/login"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Login</strong></button></a>
                                <a href="pages/register"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Sign up</strong></button></a>
                            </div>
                        </div>
                    </div>
                </div>
            </section>


            <div class="container mt-5">
                <svg viewbox="0 0 100 20">
                <defs>
                <linearGradient id="gradient">
                <stop color="#000"/>
                </linearGradient>
                <pattern id="wave" x="0" y="-0.5" width="100%" height="100%" patternUnits="userSpaceOnUse">
                    <path id="wavePath" d="M-40 9 Q-30 7 -20 9 T0 9 T20 9 T40 9 T60 9 T80 9 T100 9 T120 9 V20 H-40z" mask="url(#mask)" fill="url(#gradient)">
                    <animateTransform
                        attributeName="transform"
                        begin="0s"
                        dur="1.5s"
                        type="translate"
                        from="0,0"
                        to="40,0"
                        repeatCount="indefinite" />
                    </path>
                </pattern>
                </defs>
                <text text-anchor="middle" x="50" y="12" font-size="6" fill="white" fill-opacity="0.1">OUR TOP PRODUCTS</text>
                <text text-anchor="middle" x="50" y="12" font-size="6" fill="url(#wave)"  fill-opacity="1">OUR TOP PRODUCTS</text>
                </svg>
                <div class="row">
                    <%
                    List<Sales> salesList = dao.IndexDao.getTopProducts();
                    if (salesList != null) {
                        for (Sales sale : salesList) {
                            if (sale.getProductImage() != null) { // Add null check here
                                String pictureBase64 = java.util.Base64.getEncoder().encodeToString(sale.getProductImage());
                                String imageSrc = "data:image/png;base64," + pictureBase64;
                    %>
                    <div class="col">
                        <div class="card">
                            <a href="pages/products/viewProduct?product_id=<%=sale.getProductId()%>">
                                <figure>
                                    <img src="<%=imageSrc%>" alt="<%=sale.getProductName()%>" class="imagetop">
                                    <figcaption><%=sale.getProductName()%></figcaption>
                                </figure>
                            </a>
                        </div>
                    </div>
                    <%
                            }
                        }
                    }
                    %>
                </div>
            </div>
        </main>

        <jsp:include page="/defaults/footer.jsp" />
    </body>

</html>