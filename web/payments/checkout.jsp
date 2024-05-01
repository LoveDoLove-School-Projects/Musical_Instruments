<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="entities.Carts" %>
<%@ page import="java.util.Base64"%>
<%@ page import="utilities.FileUtilities"%>
<%
response.setHeader("Cache-Control", "no-store");
%>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Checkout</title>
    </head>

    <body>
        <main>
            <h2 class="text-center">Checkout</h2>
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
                        </tr>
                    </thead>

                    <tbody class="text-center">
                        <%
                            int numberCart=0;
                            double subTotal=0;
                            List<Carts> cartsDetails = (List<Carts>) request.getAttribute("cartList");
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
                        </tr>
                        <%
                            }
                        %>
                    </tbody>

                    <div class="row justify-content-center align-items-center">
                        <tfoot class="col-12 text-center" style="float:right;">
                            <tr class="row">
                                <th class="col-10 d-flex justify-content-end">Subtotal :</th>
                                <th class="col-2">RM ${subtotal}</th>
                            </tr>
                            <tr class="row">
                                <th class="col-10 d-flex justify-content-end">Shipping fee :</th>
                                <th class="col-2">RM ${shipping}</th>
                            </tr>
                            <tr class="row">
                                <th class="col-10 d-flex justify-content-end">Tax 10% :</th>
                                <th class="col-2">RM ${tax}</th>
                            </tr>
                            <tr class="row">
                                <th class="col-10 d-flex justify-content-end">Total :</th>
                                <th class="col-2 ">RM ${total}</th>
                            </tr>
                        </tfoot>
                    </div>
                </table>
            </section>
        </main>

        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <form action="payments/processTransaction" method="post">

                            <h2>Select Payment Method</h2>
                            <div class="form-group">
                                <label for="payment_method">Payment Method</label>
                                <select class="form-control" id="payment_method">
                                    <option value="" selected>Select Payment Method</option>
                                    <option value="CreditOrDebitCard">Credit / Debit Card</option>
                                    <option value="Paypal">PayPal</option>
                                    <option value="CashOnDelivery">Cash On Delivery</option>
                                    <!-- Add more options as needed -->
                                </select>
                            </div>
                            <div id="cardDetails" style="display: none;">
                                <div class="form-group">
                                    <label for="cardHolderName">Card Holder Name</label>
                                    <input type="text" class="form-control" id="cardHolderName" />
                                </div>
                                <div class="form-group row">
                                    <label for="cardNumber" class="col-form-label">Card Number</label>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card1" id="card1" maxlength="4" pattern="[0-9]{4}" title="First 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card2" id="card2" maxlength="4" pattern="[0-9]{4}" title="Second 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card3" id="card3" maxlength="4" pattern="[0-9]{4}" title="Third 4 digits" size="4" />
                                    </div>
                                    <div class="col-auto text-center my-auto">
                                        -
                                    </div>
                                    <div class="col">
                                        <input type="text" class="form-control" name="card4" id="card4" maxlength="4" pattern="[0-9]{4}" title="Fourth 4 digits" size="4" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="cvv">CVV</label>
                                    <input type="text" name="CVV" class="form-control" id="CVV" />
                                </div>
                                <div class="form-group row">
                                    <label for="expDate" class="col-form-label">Expiration Date</label>
                                    <div class="col">
                                        <select class="form-control" id="expYear">
                                            <option value="">Year</option>
                                            <c:forEach var="year" begin="<%= Calendar.getInstance().get(Calendar.YEAR) %>" end="<%= Calendar.getInstance().get(Calendar.YEAR) + 30 %>">
                                                <option value="${year}">${year}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col">
                                        <select class="form-control" id="expMonth">
                                            <option value="">Month</option>
                                            <c:forEach var="month" begin="1" end="12">
                                                <option value="${month}">${month}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="transaction_id" value="${transaction_id}" />
                            <input type="hidden" name="parent_order_id" value="${parent_order_id}" />
                            <input type="hidden" name="total_amount" value="${total_amount}" />
                            <input type="hidden" name="date_created_gmt" value="${date_created_gmt}" />
                            <button type="submit" class="btn btn-primary mt-3" id="paymentButton">Pay Now</button>
                        </form>
                        <form action="payments/paypal" method="post" id="paypal-button-form">
                            <input type="image" src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" alt="Paypal Button" class="mt-3" />
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <script>
            const payment_method = ['CreditOrDebitCard', 'Paypal', 'CashOnDelivery'];
            $(document).ready(function () {
                $('#paypal-button-form').hide();
                $('#payment_method').change(function () {
                    if ($(this).val() === payment_method[0]) {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }

                    if ($(this).val() === payment_method[1]) {
                        $('#paymentButton').hide();
                        $('#paypal-button-form').show();
                    } else {
                        $('#paypal-button-form').hide();
                        $('#paymentButton').show();
                    }
                });
            });
        </script>
    </body>

</html>