<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<!DOCTYPE html>
<html>

    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Transaction Page</title>
    </head>

    <body>
        <jsp:include page="/defaults/header.jsp" />
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
                        <form>
                            <div class="form-group">
                                <label for="paymentMethod">Payment Method</label>
                                <select class="form-control" id="paymentMethod">
                                    <option value="">Select Payment Method</option>
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
                                <div class="form-group">
                                    <label for="cardNumber">Card Number</label>
                                    <input type="text" class="form-control" id="cardNumber" />
                                </div>
                                <div class="form-group">
                                    <label for="cvv">CVV</label>
                                    <input type="text" class="form-control" id="cvv" />
                                </div>
                                <div class="form-group">
                                    <label for="expDate">Expiration Date</label>
                                    <input type="text" class="form-control" id="expDate" />
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="/defaults/footer.jsp" />
        <script>
            $(document).ready(function () {
                $('#paymentMethod').change(function () {
                    if ($(this).val() === 'Credit Card' || $(this).val() === 'Debit Card') {
                        $('#cardDetails').show();
                    } else {
                        $('#cardDetails').hide();
                    }
                });
            });
        </script>
    </body>

</html>