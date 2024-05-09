<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<jsp:useBean id="transaction" class="entities.PaypalPayment" scope="session" />
<jsp:useBean id="payer" class="entities.PaypalPayment" scope="session" />
<%
response.setHeader("Cache-Control", "no-store");
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Review</title>
    </head>
    <body>
        <div class="container">
            <div class="py-5 text-center">
                <h1>Paypal Payment Review Before Paying</h1>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <h4 class="mb-3">Transaction Details</h4>
                    <form action="payments/paypal/execute" method="post">
                        <input type="hidden" name="paymentId" value="${param.paymentId}" />
                        <input type="hidden" name="PayerID" value="${param.PayerID}" />
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th scope="row">Description:</th>
                                    <td>${transaction.description}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Subtotal:</th>
                                    <td>MYR ${transaction.amount.details.subtotal}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Shipping:</th>
                                    <td>MYR ${transaction.amount.details.shipping}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Tax:</th>
                                    <td>MYR ${transaction.amount.details.tax}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Total:</th>
                                    <td>MYR ${transaction.amount.total}</td>
                                </tr>
                            </tbody>
                        </table>
                        <h4 class="mb-3">Paypal Payer Information</h4>
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th scope="row">First Name:</th>
                                    <td>${payer.first_name}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Last Name:</th>
                                    <td>${payer.last_name}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Email:</th>
                                    <td>${payer.email}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="col">
                                <button class="btn btn-primary btn-lg btn-block" type="submit" id="payNow">Pay Now</button>
                            </div>
                            <div class="col">
                                <button type="button" class="btn btn-primary btn-lg btn-block" id="cancel">Cancel</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script type="module">
            $("#cancel").click(function (event) {
                event.preventDefault();
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, cancel it!'
                }).then((result) => {
                    if (result.value) {
                        window.location.href = "${basePath}payments/cancel?transaction_number=${param.paymentId}";
                                    }
                                });
                            });
                            $("#payNow").click(function (event) {
                                event.preventDefault();
                                Swal.fire({
                                    title: 'Are you sure?',
                                    text: "You won't be able to revert this!",
                                    icon: 'warning',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: 'Yes, proceed with the payment!'
                                }).then((result) => {
                                    if (result.value) {
                                        $("form").submit();
                                    }
                                });
                            });
        </script>
    </body>
</html>