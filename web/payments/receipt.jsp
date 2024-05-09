<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<jsp:useBean id="transaction" class="entities.Transactions" scope="request" />
<%
response.setHeader("Cache-Control", "no-store");
%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Receipt Details</title>
    </head>
    <body>
        <div class="container">
            <div class="py-5 text-center">
                <h2>Receipt Details</h2>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <table class="table">
                        <tbody>
                            <tr>
                                <th scope="row">Transaction Created Date:</th>
                                <td>${transaction.dateCreatedGmt}</td>
                            </tr>
                            <tr>
                                <th scope="row">Transaction Updated Date:</th>
                                <td>${transaction.dateUpdatedGmt}</td>
                            </tr>
                            <tr>
                                <th scope="row">Merchant:</th>
                                <td><%=application.getInitParameter("companyName")%></td>
                            </tr>
                            <tr>
                                <th scope="row">Transaction Number:</th>
                                <td>${transaction.transactionNumber}</td>
                            </tr>
                            <tr>
                                <th scope="row">Order Number:</th>
                                <td>${transaction.orderNumber}</td>
                            </tr>
                            <tr>
                                <th class="row">Transaction Status:</th>
                                <td>${transaction.transactionStatus}</td>
                            </tr>
                            <tr>
                                <th scope="row">Payment Method:</th>
                                <td>${transaction.paymentMethod}</td>
                            </tr>
                            <tr>
                                <th scope="row">Amount:</th>
                                <td>MYR ${transaction.totalAmount}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 order-md-1">
                    <button onclick="window.print()" class="btn btn-primary">Print Receipt</button>
                    <a href="${basePath}pages/transactionHistory" class="btn btn-primary">Go back to TAR Music</a>
                </div>
            </div>
        </div>
    </body>
</html>