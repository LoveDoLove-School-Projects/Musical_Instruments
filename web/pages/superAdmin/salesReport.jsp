<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Sales"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>View Sales</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <div class="container">
            <div class="panel-heading">
                <br><h1><i class="fas fa-chart-line">  View Sales</i></h1>
            </div>
            <div class="container mt-4">
                <div class="row mt-4">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-body">
                                <table class="table">
                                    <thead>
                                        <tr><th>Sales Report for Date: <% if ("day".equals(request.getAttribute("reportType")) || request.getAttribute("date") != null) { %>${startingDate}<% } else { %> ${startingDate} until ${endingDate}<% } %></th></tr>
                                        <tr>
                                            <th>Transaction Number</th>
                                            <th style="text-align: center">Total Amount (RM)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="sales" items="${salesList}">
                                            <tr>
                                                <td>${sales.getProductName()}</td>
                                                <td style="text-align: center">${sales.getTotalAmount()}</td>
                                            </tr>
                                        </c:forEach>
                                        <tr><td> </td> <td> </td></tr>
                                        <tr>
                                            <td><strong>Total Revenue</strong></td>
                                            <td style="text-align: center"><strong>${totalRevenue}</strong></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="col-md-2">
                                    <a href="pages/superAdmin/viewSales" class="btn btn-primary mr-2">Go back</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
