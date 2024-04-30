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
        <style type="text/css">
            table {
                border: 0;
            }
            table td {
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div align="center">
            <h1>Payment Done. Thank you for purchasing our products</h1>
            <br/>
            <h2>Receipt Details:</h2>
            <table>
                <tr>
                    <td><b>Merchant:</b></td>
                    <td>Musical Instruments</td>
                </tr>
                <tr>
                    <td><b>Payer:</b></td>
                    <td>${payer.first_name} ${payer.last_name}</td>
                </tr>
                <tr>
                    <td><b>Description:</b></td>
                    <td>${transaction.description}</td>
                </tr>
                <tr>
                    <td><b>Subtotal:</b></td>
                    <td>MYR ${transaction.amount.details.subtotal}</td>
                </tr>
                <tr>
                    <td><b>Shipping:</b></td>
                    <td>MYR ${transaction.amount.details.shipping}</td>
                </tr>
                <tr>
                    <td><b>Tax:</b></td>
                    <td>MYR ${transaction.amount.details.tax}</td>
                </tr>
                <tr>
                    <td><b>Total:</b></td>
                    <td>MYR ${transaction.amount.total}</td>
                </tr>
            </table>
        </div>
    </body>
</html>