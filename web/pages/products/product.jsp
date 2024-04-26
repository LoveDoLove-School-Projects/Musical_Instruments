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
        <link rel="stylesheet" href="assets/css/product.css" />
    </head>


    <body>
        <jsp:include page="/defaults/header.jsp" />
        <main class="main">
            <section class="section1">
                <div class="container">
                    <div class="row justify-content-center align-items-center">
                        <div class="d-block my-auto p-5 col-12 col-xl-5" style="animation:swing 1s">
                            <h1>Welcome to music instruments</h1>
                            <p>Enjoy the music, Malaysia top 1 music website</p>
                            <button class="m-2 px-4 py-2 button"><strong>Shop now</strong></button>
                            <button class="m-2 px-4 py-2 button"><strong>Explore</strong></button>
                        </div>

                        <div class="col-12 col-xl-7">
                            <div class="m-3 mx-auto" style="animation:bounceInRight 2s">
                                <img src>
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
                                <a href="#"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Login</strong></button></a>
                                <a href="#"><button class="m-2 px-4 py-2 PremiumBoxBtn"><strong>Sign up</strong></button></a>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section3">
                <div class="container-fluid">
                    <div class="row m-2 m-xxl-5 p-0 p-xxl-5 d-flex align-items-center justify-content-center">
                        <div class="col-12 col-xxl-12">
                            <div class="m-5 d-block text-center" style="animation:fadeInUp 5s">
                                <h1>This is our product that we have</h1>
                                <h5>Here have almost 10 category to let you choose</h5>
                            </div>
                        </div>

                        <!--first row-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">PIANO</h3>
                            <div class="row">
                                <%
                        List<Products> pianoProductDetails = (List<Products>) request.getAttribute("pianoProductDetails");
                        for (Products product : pianoProductDetails) {
               byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImagePath());
               String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
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
                                %>            
                            </div>
                        </div>

                        <!--second row-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="col-12 text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">Guitars</h3>
                            <div class="row">
                                <%
                           List<Products> guitarProductDetails = (List<Products>) request.getAttribute("guitarProductDetails");
                           for (Products product : guitarProductDetails) {
                  byte[] pictureBytes = FileUtilities.readDirectoryContent(IMAGE_DEFAULT_PATH + product.getImagePath());
                  String pictureBase64 = Base64.getEncoder().encodeToString(pictureBytes);
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
                                %>        
                            </div>
                        </div>

                        <!--third row-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="col-12 text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">Drums</h3>
                            <div class="row">


                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section4 class="section4">
                <div class="">

                </div>
            </section4>

        </main>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
