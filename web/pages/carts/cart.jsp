<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="java.util.Base64"%>
<%@ page import="utilities.FileUtilities"%>
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
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />

        <style>
            .Title{
                border-bottom: solid black 3px;
            }

            .TitleBar{
                background-color: aliceblue;
            }

            .CheckoutBar{
                position: fixed;
                border-top: solid black 1px;
                background-color: white;
            }
            .chkOutPrice{
                color: rgb(238, 59, 59);
            }

            .ChkoutBtn{
                background-color: rgb(238, 59, 59);
                border: none;
                box-shadow: 3px 3px 5px;
                color: rgb(255, 253, 253);
                font-size: 20px;
            }

            .editBtn{
                background-color: green;
            }

            .deleteBtn{
                background-color: red;
            }
        </style>
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
                            <th class="col p-3 Title">Edit</th>
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
                                    <form method="POST" action="spages/carts/editCart">
                                        <button type="submit" class="col-6 editBtn"><strong>EDIT</strong></button>
                                        <input type="hidden" name="cartId" value="<%=carts.getCartId()%>"/>
                                    </form>
                                    <form method="POST" action="">
                                        <button class="col-6 deleteBtn" type="submit"><strong>DELETE</strong></button>
                                    </form>
                                </div>
                            </th>


                        </tr>

                        <%
                            }
                        %>
                    </tbody>
                    <!--                    <div class="row justify-content-center align-items-center">
                                            <tfoot class="col-12 text-center" style="float:right;">
                                                <tr class="d-flex justify-content-end">
                                                    <th class="col">Subtotal :</th>
                                                    <th class="col">RM subtotal</th>
                                                </tr>
                                                <tr class="d-flex justify-content-end">
                                                    <th class="col">delivery fee :</th>
                                                    <th class="col">RM4.99</th>
                                                </tr>
                                            </tfoot>
                                        </div>-->
                </table>

                <div class="w-100 fixed-bottom CheckoutBar">
                    <div class="row align-items-center justify-content-center">
                        <h4 class="col-10 d-flex justify-content-end">Total : <strong class="chkOutPrice">  RM<%=String.format("%.2f",subTotal)%></strong></h4>
                        <form action="payments/checkout" method="post" class="col-2">
                            <input type="submit" class=" p-4 text-center ChkoutBtn" name="W-100 chekout" value="Check Out">
                        </form>
                    </div>
                </div>
            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
