<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Transaction</title>
    </head>

    <body>
        <form action="/payments/processTransaction" method="post">
            <section class="py-5">
                <div class="container justify-content-center align-items-center">
                    <div class="row">
                        <div class="col-md-6">
                            <h2>Transaction Details</h2>
                            <p><strong>Transaction ID:</strong> ${transaction_id}</p>
                            <p><strong>Order ID:</strong> ${parent_order_id}</p>
                            <p><strong>Amount:</strong> ${total_amount}</p>
                            <p><strong>Date: ${date_created_gmt}</strong></p>
                            <!-- Add more details as needed -->
                        </div>
                        <input type="hidden" name="transaction_id" value="${transaction_id}" />
                        <input type="hidden" name="parent_order_id" value="${parent_order_id}" />
                        <input type="hidden" name="total_amount" value="${total_amount}" />
                        <input type="hidden" name="date_created_gmt" value="${date_created_gmt}" />
                    </div>
                </div>
            </section>
            <section class="py-5">
                <div class="container justify-content-center align-items-center">
                    <div class="row">
                        <div class="col-md-6">
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
                                    <input type="text" class="form-control" id="cvv" />
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
                            <div id="paypal-button-container" style="display: none;"></div>
                            <button type="submit" class="btn btn-primary mt-3" data-id="paymentButton">Pay Now</button>
                        </div>
                    </div>
                </div>
            </section>
        </form>
        <script src="https://www.paypal.com/sdk/js?client-id=AV2LS6xYVmbVxvE8GrMwyhzi2zopTUPEbkybzONAXYK5t4oNLScaUD3XW-4aybvisbntDIbePzhYObtJ&currency=MYR&disable-funding=credit,card"></script>
        <script src="payments/transaction.js"></script>
        <script>
            const payment_method = ['CreditOrDebitCard', 'Paypal', 'CashOnDelivery'];
            $(document).ready(function () {
                $('#paypal-button-container').hide();
                $('#payment_method').change(function () {
                    if ($(this).val() === payment_method[0]) {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }

                    if ($(this).val() === payment_method[1]) {
                        $('#paypal-button-container').show();
                    } else {
                        $('#paypal-button-container').hide();
                        paypal.Buttons().close();
                    }
                });
            });
            let transaction_id = $('#transaction_id');
            let parent_order_id = $('#parent_order_id');
            let total_amount = $('#total_amount');
            let date_created_gmt = $('#date_created_gmt');
            paypal
                    .Buttons({
                        style: {
                            shape: "rect",
                            color: "gold",
                            layout: "vertical",
                            label: "pay",
                            tagline: false,
                        },
                        onClick: function (data, actions) {},
                        createOrder: function (data, actions) {
                            return actions.order.create({
                                purchase_units: [
                                    {
                                        description: `Transaction ID: ${transaction_id.value}`,
                                        amount: {
                                            currency_code: "MYR",
                                            value: "200",
                                        },
                                    },
                                ],
                            });
                        },
                        onApprove: function (data, actions) {
                            return actions.order.capture().then(function (details) {
                                alert("Transaction completed by " + details.payer.name.given_name);
                                return fetch("/payments/processTransaction", {
                                    method: "post",
                                    headers: {
                                        "content-type": "application/json",
                                    },
                                    body: JSON.stringify({
                                        orderID: data.orderID,
                                        payerID: data.payerID,
                                        transactionID: transaction_id.value,
                                    }),
                                });
                            });
                        },
                        onError: function (error) {
                            console.error("PayPal error:", error);
                        },
                    })
                    .render("#paypal-button-container");
        </script>
    </body>

</html>