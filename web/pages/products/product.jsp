<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Products" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Products Page</title>
        <link rel="stylesheet" href="assets/css/product.css" />
    </head>


    <body>
        <jsp:include page="/defaults/header.jsp" />
        <main class="main" data-bs-spy="scroll" data-bs-target=".d-block">
            <section class="section3">
                <div class="container-fluid">
                    <div class="row m-2 m-xxl-5 p-0 p-xxl-5 d-flex align-items-center justify-content-center">
                        <div class="col-12 col-xxl-12 productTitle"  id="section3">
                            <div class="m-5 d-block text-center" style="animation:fadeInUp 5s">
                                <h1>This is our product that we have</h1>
                                <h5>Here have 4 category of products to let you choose</h5>
                            </div>
                        </div>

                        <!--first category-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">PIANO</h3>
                            <div class="row">
                                <%
                        List<Products> pianoProductDetails = (List<Products>) request.getAttribute("pianoProductDetails");
                        for (Products product : pianoProductDetails) {
                        String pictureBase64 = Base64.getEncoder().encodeToString(product.getImage());
                         String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                         if(product.getQuantity()!=0){
                                %>
                                <div class="col-6 col-xl-3 col-xxl-3">
                                    <a href="pages/products/viewProduct?product_id=<%=product.getProductId()%>" class="MusicInstruments row1-MusicInstruments1">
                                        <div class="pt-4 d-flex justify-content-center">
                                            <img src="<%=imageSrc%>" class="img-fluid w-50 d-flex text-center justify-self-center align-self-center" loading="lazy">
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

                        <!--second category-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="col-12 text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">GUITARS</h3>
                            <div class="row">
                                <%
                           List<Products> guitarProductDetails = (List<Products>) request.getAttribute("guitarProductDetails");
                           for (Products product : guitarProductDetails) {
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
                                %>
                            </div>
                        </div>

                        <!--third row-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="col-12 text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">DRUMS</h3>
                            <div class="row">
                                <%
                           List<Products> drumProductDetails = (List<Products>) request.getAttribute("drumProductDetails");
                           for (Products product : drumProductDetails) {
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
                                %>
                            </div>
                        </div>

                        <!--four category-->
                        <div class="my-5 col-12 col-xxl-12">
                            <h3 class="col-12 text-center" style="animation:backInDown 3s;text-shadow: 0 1px 0 #ccc,0 2px 0 #c9c9c9,0 3px 0 #bbb,0 4px 0 #b9b9b9,0 5px 0 #aaa,0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2),0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15);">VIOLINS</h3>
                            <div class="row">
                                <%
                           List<Products> violinProductDetails = (List<Products>) request.getAttribute("violinProductDetails");
                           for (Products product : violinProductDetails) {
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
                                %>
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
