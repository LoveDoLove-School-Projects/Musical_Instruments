<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
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
                        <h4 class="mb-3">Paypal Shipping Address</h4>
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th scope="row">Recipient Name:</th>
                                    <td>${shippingAddress.recipient_name}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Line 1:</th>
                                    <td>${shippingAddress.line1}</td>
                                </tr>
                                <tr>
                                    <th scope="row">City:</th>
                                    <td>${shippingAddress.city}</td>
                                </tr>
                                <tr>
                                    <th scope="row">State:</th>
                                    <td>${shippingAddress.state}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Country Code:</th>
                                    <td>${shippingAddress.country_code}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Postal Code:</th>
                                    <td>${shippingAddress.postal_code}</td>
                                </tr>
                            </tbody>
                        </table>
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Pay Now</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>