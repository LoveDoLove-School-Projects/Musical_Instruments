<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/" />
<%@ page import="java.util.List"%>
<%@ page import="entities.Transactions"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="/defaults/head.jsp" />
        <title>Transaction History</title>
    </head>
    <body>
        <jsp:include page="/defaults/header.jsp" />
        <%
        List<Transactions> transactionList = (List<Transactions>) request.getAttribute("transactionList");
        %>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <br><h3 class="panel-title"><i class="fas fa-file-invoice">  Transaction  History</i></h3>
                        </div>
                        <div class="panel-body table-responsive">
                            <table id="viewTransactionTable" class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Order No.</th>
                                        <th>Txn Number</th>
                                        <th>Customer ID</th>
                                        <th>Payment Method</th>
                                        <th>Currency</th>
                                        <th>Total Amount</th>
                                        <th>Txn Status</th>
                                        <th>Txn Created</th>
                                        <th>Txn Updated</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (transactionList != null) {
                                        for (Transactions transaction : transactionList) {
                                    %>
                                    <tr>
                                        <td><%=transaction.getOrderNumber()%></td>
                                        <td><%=transaction.getTransactionNumber()%></td>
                                        <td><%=transaction.getUserId()%></td>
                                        <td><%=transaction.getPaymentMethod()%></td>
                                        <td><%=transaction.getCurrency()%></td>
                                        <td><%=transaction.getTotalAmount()%></td>
                                        <td><%=transaction.getTransactionStatus()%></td>
                                        <td><%=transaction.getDateCreatedGmt()%></td>
                                        <td><%=transaction.getDateUpdatedGmt()%></td>
                                    </tr>
                                    <% }
                                    } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/defaults/footer.jsp" />
        <script type="module">
            $(document).ready(function () {
                $('#viewTransactionTable').DataTable();
            });
        </script>
    </body>
</html>