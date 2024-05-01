<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Carts" %>
<%
int numberCart=0;
double subTotal=0;
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Cart Page</title>
        <link rel="stylesheet" href="assets/css/cart.css" />
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <main>
            <section class="section">
                <table class="table">
                    <thead class="text-center TitleBar">
                        <tr class="row justify-content-center align-content-center">
                            <th class="col p-3 Title">No.</th>
                            <th class="col p-3 Title">Image</th>
                            <th class="col p-3 Title">Product Name</th>
                            <th class="col p-3 Title">Product Color</th>
                            <th class="col p-3 Title">Quantity</th>
                            <th class="col p-3 Title">Price</th>
                            <th class="col p-3 Title">Total Price</th>
                            <th class="col p-3 Title">Function</th>
                        </tr>
                    </thead>

                    <tbody class="text-center">
                        <%
                              List<Carts> cartsDetails = (List<Carts>) request.getAttribute("cartDetails");
                              for (Carts carts : cartsDetails) {
                              numberCart++;
                              subTotal+=carts.getProductTotalprice();
                              String pictureBase64 = Base64.getEncoder().encodeToString(carts.getProductImage());
                              String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                        %>
                        <tr class="row justify-content-center">

                            <th class="col"><%=numberCart%></th>
                            <th class="col"><img src="<%=imageSrc%>" class="img-fluid w-50 mx-auto"></th>
                            <th class="col "><%=carts.getProductName()%></th>
                            <th class="col"><%=carts.getProductColor()%></th>
                            <th class="col"><%=carts.getProductQuantity()%></th>
                            <th class="col">RM<%=carts.getProductPrice()%></th>
                            <th class="col">RM<%=carts.getProductTotalprice()%></th>
                            <th class="col">
                                <div class="row align-content-center justify-content-center">
                                    <a href="pages/carts/editCart?cart_id=<%=carts.getCartId()%>" class="MusicInstruments row1-MusicInstruments1">
                                        <button type="button" class="w-50 mx-auto my-1 p-2 editBtn"><strong>Edit</strong></button>
                                    </a>
                                </div>
                                <form method="POST" action="pages/carts/deleteCartServlet"  class="deleteCartForm" id="deleteCartForm">
                                    <input type="hidden" name="cartId" value="<%=carts.getCartId()%>"/>
                                    <button type="submit" class="w-50 mx-auto my-1 p-2 deleteBtn"><strong>Delete</strong></button>
                                </form>
                            </th>


                        </tr>

                        <%
                            }
                        %>
                    </tbody>
                </table>

                <div class="w-100 fixed-bottom CheckoutBar">
                    <div class="row align-items-center justify-content-center">
                        <h4 class="col-10 d-flex justify-content-end">Total : <strong class="chkOutPrice">  RM<%=String.format("%.2f",subTotal)%></strong></h4>
                        <form action="payments/checkout" method="post" class="col-2 text-center">
                            <input type="submit" class=" p-4 text-center ChkoutBtn" name="chekout" value="Check Out">
                        </form>
                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module" src="assets/js/carts.js"></script>
    </body>
</html>
