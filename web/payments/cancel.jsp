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
        <title>Transaction Cancelled</title>
        <style>
            body {
                background-color: #f8f9fa;
                padding-top: 50px;
                text-align: center;
            }

            .container {
                max-width: 400px;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h2>Transaction Cancelled</h2>
            <p>Your transaction has been cancelled. If you have any questions, please contact support.</p>
            <a href="${basePath}payments/receipt?transaction_number=${transaction_number}">Continue to Receipt</a>
        </div>
    </body>

</html>
