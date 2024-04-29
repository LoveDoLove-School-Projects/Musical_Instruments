<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="entities.Carts" %>
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
        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <h2>Checkout</h2>
                    </div>
                </div>
            </div>
        </section>
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
                            <input type="hidden" name="upload" value="1" />
                            <input type="hidden" name="return" value="<%=application.getInitParameter("returnurl")%>" />
                            <input type="hidden" name="cmd" value="_cart" />
                            <input type="hidden" name="business" value="<%=application.getInitParameter("business")%>" />

                            <!-- Product 1 sample -->
                            <!-- <input type="hidden" name="item_name_1" value="Item Name 1" />
                            <input type="hidden" name="item_number_1" value="p1" />
                            <input type="hidden" name="amount_1" value="2" />
                            <input type="hidden" name="quantity_1" value="3" /> -->

                            <%
                                List<Carts> cartList = (List<Carts>) request.getAttribute("cartList");
                                int i = 1;
                                for (Carts cart : cartList) {
                            %>
                            <input type="hidden" name="item_name_<%=i%>" value="<%=cart.getProductName()%>" />
                            <input type="hidden" name="item_number_<%=i%>" value="<%=cart.getProductId()%>" />
                            <input type="hidden" name="amount_<%=i%>" value="<%=cart.getProductPrice()%>" />
                            <input type="hidden" name="quantity_<%=i%>" value="<%=cart.getProductQuantity()%>" />
                            <%
                                i++;
                                }
                            %>

                            <!-- Image -->
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