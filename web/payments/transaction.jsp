<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!-- IMport -->
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Transaction Page</title>
    </head>

    <body>
        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <h2>Transaction Details</h2>
                        <p><strong>Transaction ID:</strong> ${transactionId}</p>
                        <p><strong>Order ID:</strong> ${orderId}</p>
                        <p><strong>Invoice ID:</strong> ${invoiceId}</p>
                        <p><strong>Amount:</strong> ${amount}</p>
                        <!-- Add more details as needed -->
                    </div>
                </div>
            </div>
        </section>
        <section class="py-5">
            <div class="container justify-content-center align-items-center">
                <div class="row">
                    <div class="col-md-6">
                        <h2>Select Payment Method</h2>
                        <form action="/payments/payTransaction" method="post">
                            <div class="form-group">
                                <label for="paymentMethod">Payment Method</label>
                                <select class="form-control" id="paymentMethod">
                                    <option value="" selected>Select Payment Method</option>
                                    <option value="Credit Card">Credit Card</option>
                                    <option value="Debit Card">Debit Card</option>
                                    <option value="PayPal">PayPal</option>
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
                                        <!-- First year block -->
                                        <select class="form-control" id="expYear">
                                            <option value="">Year</option>
                                            <c:forEach var="year" begin="<%= Calendar.getInstance().get(Calendar.YEAR) %>" end="<%= Calendar.getInstance().get(Calendar.YEAR) + 30 %>">
                                                <option value="${year}">${year}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col">
                                        <!-- Second date block -->
                                        <select class="form-control" id="expMonth">
                                            <option value="">Month</option>
                                            <c:forEach var="month" begin="1" end="12">
                                                <option value="${month}">${month}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Pay Now</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <script>
            const paymentMethod = ['Credit Card', 'Debit Card', 'PayPal'];
            $(document).ready(function () {
                $('#paymentMethod').change(function () {
                    if ($(this).val() === paymentMethod[0] || $(this).val() === paymentMethod[1]) {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }
                });
            });
        </script>
    </body>

</html>