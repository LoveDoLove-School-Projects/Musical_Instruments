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
                            <h3 class="panel-title">Transaction History</h3>
                            <button id="exportButton" class="btn btn-primary md-3">Export to CSV</button>
                        </div>
                        <div class="panel-body table-responsive">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>Txn Number</th>
                                        <th>Order Number</th>
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
                                    <tr class="clickable-row" data-href="payments/receipt?transaction_number=<%=transaction.getTransactionNumber()%>">
                                        <td><%=transaction.getTransactionNumber()%></td>
                                        <td><%=transaction.getOrderNumber()%></td>
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
        <script>
            $(document).ready(function () {
                $('.clickable-row').click(function () {
                    window.open($(this).data('href'), '_blank');
                });
            });
            document.getElementById('exportButton').addEventListener('click', function () {
                var table = document.querySelector('table');
                var data = [];
                for (var row of table.rows) {
                    var rowData = [];
                    for (var cell of row.cells) {
                        rowData.push('"' + cell.textContent + '"'); // wrap cell text in quotes to handle commas in cell text
                    }
                    data.push(rowData.join(','));
                }
                var csv = data.join('\n');
                var blob = new Blob([csv], {type: 'text/csv;charset=utf-8;'});
                saveAs(blob, 'transaction_history.csv');
            });
        </script>
    </body>
</html>