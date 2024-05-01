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
        <title>Payment Receipt</title>
    </head>
    <body>
        <div class="container">
            <div class="py-5 text-center">
                <h2>Payment Done. Thank you for purchasing our products</h2>
                <p class="lead">Receipt Details:</p>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <h4 class="mb-3">Billing address</h4>
                    <table class="table">
                        <tbody>
                            <tr>
                                <th scope="row">Merchant:</th>
                                <td>Musical Instruments</td>
                            </tr>
                            <tr>
                                <th scope="row">Payer:</th>
                                <td>${payer.first_name} ${payer.last_name}</td>
                            </tr>
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
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <a href="${basePath}" class="btn btn-primary">Return to Main Page</a>
                </div>
            </div>
        </div>
    </body>
</html>