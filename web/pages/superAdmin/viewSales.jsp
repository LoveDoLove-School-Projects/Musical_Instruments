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
                <!-- Sales Records Form -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                View Sales Records
                            </div>
                            <div class="card-body">
                                <form action="pages/superAdmin/viewSales" method="post">
                                    <input type="hidden" name="action" value="specificReport">
                                    <div class="form-group">
                                        <label for="date">Select Date:</label>
                                        <input type="date" id="date" name="date" class="form-control">
                                    </div>
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Generate</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- Sales Report Form -->
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                Generate Sales Report
                            </div>
                            <div class="card-body">
                                <form action="pages/superAdmin/viewSales" method="post">
                                    <input type="hidden" name="action" value="generateReport">
                                    <div class="form-group">
                                        <label for="reportType">Report Type:</label>
                                        <select name="reportType" id="reportType" class="form-control">
                                            <option value="day">By Day</option>
                                            <option value="month">By Month</option>
                                            <option value="year">By Year</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-success">Generate</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mt-4">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header">
                                <strong>Top 10 Sold Products</strong>
                            </div>
                            <div class="card-body">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Product Name</th>
                                            <th>Total Sales</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="sales" items="${top10Products}">
                                            <tr>
                                                <td>${sales.getProductName()}</td>
                                                <td>${sales.getTotalQuantity()}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
    </body>
</html>
