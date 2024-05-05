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
        <title>Verify</title>
    </head>
    <body>
        <div class="container">
            <div class="py-5 text-center">
                <h1>Please Verify Before Paying</h1>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <h4 class="mb-3">Transaction Details</h4>
                    <form action="payments/ccdc/verify" method="post">
                        <!-- Create a simple credit / debit card otp verify form -->
                        <input type="hidden" name="paymentId" value="${param.paymentId}" />
                        <input type="hidden" name="PayerID" value="${param.PayerID}" />
                        <!-- show txn number and some basic txn details -->
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
                        <h4 class="mb-3">Payer Information</h4>
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
                                <tr>
                                    <th scope="row">Phone:</th>
                                    <td>${payer.phone}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="otp">OTP</label>
                                <input type="text" class="form-control" id="otp" name="otp" placeholder="OTP" required>
                            </div>
                        </div>
                        <hr class="mb-4">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Verify</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>