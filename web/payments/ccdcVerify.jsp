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
                    <form action="payments/ccdc/verify" method="post" id="verifyForm">
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th scope="row">Transaction Number:</th>
                                    <td>${transaction.getTransactionNumber()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Order Number:</th>
                                    <td>${transaction.getOrderNumber()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Subtotal:</th>
                                    <td>MYR ${orderDetails.getSubtotal()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Shipping:</th>
                                    <td>MYR ${orderDetails.getShipping()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Tax:</th>
                                    <td>MYR ${orderDetails.getTax()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Total:</th>
                                    <td>MYR ${orderDetails.getTotal()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <h4 class="mb-3">Payer Information</h4>
                        <table class="table">
                            <tbody>
                                <tr>
                                    <th scope="row">First Name:</th>
                                    <td>${customer.getFirstName()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Last Name:</th>
                                    <td>${customer.getLastName()}</td>
                                </tr>
                                <tr>
                                    <th scope="row">Email:</th>
                                    <td>${customer.getEmail()}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="otp">OTP</label>
                                <input type="text" name="otp" id="otp" class="form-control form-control-lg" maxlength="6" placeholder="XXXXXX" required />
                            </div>
                        </div>
                        <button class="btn btn-primary btn-lg btn-block md-3" type="submit" id="submitButton">Verify</button>
                    </form>
                </div>
            </div>
        </div>
        <script src="assets/js/ccdc.js"></script>
    </body>
</html>