<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Orders"%>
<%@ page import="java.util.Base64"%>
<%@ page import="entities.Transactions"%>
<%@ page import="jakarta.persistence.EntityManager"%>
<%
int numberOrder=0;
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
        <style>
            .orderHistoryRecipt{
                border: solid 2px black;
                border-radius: 20px;
            }

        </style>

        <main>
            <section class="section">
                <div class="container">
                    <div class="m-5">
                        <h1 class="text-center m-5">Order History Receipt</h1>
                        <table class="table orderHistoryRecipt">
                            <thead class="text-center">
                                <tr class="row justify-content-center align-content-center m-1">
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">No.</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Image</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Name</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Quantity</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Color</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Price</th>
                                    <th class="col p-3 Title" style="background-color:rgb(230, 85, 85); color:white; text-shadow: black 3px 3px 3px;">Product Total Price</th>

                                </tr>
                            </thead>

                            <tbody class="text-center">
                                <%
                                      EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
                                     List<Orders> orders = (List<Orders>) request.getAttribute("orderLists");
                                     for (Orders orderDetails : orders) {
                                      Transactions transactions = entityManager.createNamedQuery("Transactions.findByOrderNumber", Transactions.class).setParameter("orderNumber", orderDetails.getOrderNumber()).getSingleResult();
                                     numberOrder++;
                                      String pictureBase64 = Base64.getEncoder().encodeToString(orderDetails.getProductImage());
                                      String imageSrc = "data:image/png;base64," + pictureBase64; // Change "image/png" based on the actual image type
                           
                                %>

                                <tr class="row justify-content-center m-1">
                                    <th class="col"><%=numberOrder%></th>
                                    <th class="col"><img src="<%=imageSrc%>" class="img-fluid w-50 mx-auto"></th>
                                    <th class="col"><%=orderDetails.getProductName()%></th>
                                    <th class="col "><%=orderDetails.getProductQuantity()%></th>
                                    <th class="col"><%=orderDetails.getProductColor()%></th>
                                    <th class="col"><%=orderDetails.getProductPrice()%></th>
                                    <th class="col"><%=orderDetails.getProductTotalprice()%></th>
                                </tr>

                                <%
                                    }
                                %>

                            </tbody>
                            <div class="row justify-content-center align-items-center">
                                <tfoot class="col-12 text-center" style="float:right;">
                                    <tr class="row m-1">
                                        <th class="col-10 d-flex justify-content-end">Subtotal :</th>
                                        <th class="col-2">RM ${subtotal}</th>
                                    </tr>
                                    <tr class="row m-1">
                                        <th class="col-10 d-flex justify-content-end">Shipping fee :</th>
                                        <th class="col-2">RM ${shipping}</th>
                                    </tr>
                                    <tr class="row m-1">
                                        <th class="col-10 d-flex justify-content-end">Tax 10% :</th>
                                        <th class="col-2">RM ${tax}</th>
                                    </tr>
                                    <tr class="row m-1">
                                        <th class="col-10 d-flex justify-content-end">Total :</th>
                                        <th class="col-2 ">RM ${total}</th>
                                    </tr>
                                </tfoot>
                            </div>
                        </table>
                    </div>
                </div>


            </section>
        </main>
        <jsp:include page="/defaults/footer.jsp" />

    </body>
</html>