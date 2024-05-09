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
        <div class="container">
            <div class="row">
                <div class="col-md-12 m-3">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Transaction History</h3>
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
                                    <c:forEach var="transaction" items="${transactionList}">
                                        <tr class="clickable-row" data-href="payments/receipt?transaction_number=${transaction.transactionNumber}">
                                            <td>${transaction.transactionNumber}</td>
                                            <td>${transaction.orderNumber}</td>
                                            <td>${transaction.paymentMethod}</td>
                                            <td>${transaction.currency}</td>
                                            <td>${transaction.totalAmount}</td>
                                            <td>${transaction.transactionStatus}</td>
                                            <td>${transaction.dateCreatedGmt}</td>
                                            <td>${transaction.dateUpdatedGmt}</td>
                                        </tr>
                                    </c:forEach>
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
                    window.location = $(this).data("href");
                });
            });
        </script>
    </body>
</html>